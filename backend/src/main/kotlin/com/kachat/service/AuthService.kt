package com.kachat.service

import com.kachat.db.UserTable
import com.kachat.dto.LoginRequest
import com.kachat.dto.RegisterRequest
import com.kachat.models.User
import com.kachat.utils.PasswordUtil
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class AuthService {
    fun register(request: RegisterRequest): User {
        // 检查用户名是否存在
        val existing = transaction {
            UserTable.select { UserTable.username eq request.username }.singleOrNull()
        }
        if (existing != null) {
            throw RuntimeException("用户名已被注册")
        }

        val hashed = PasswordUtil.hash(request.password)
        val newId = transaction {
            UserTable.insertAndGetId {
                it[username] = request.username
                it[password] = hashed
                it[createdAt] = java.time.LocalDateTime.now()
                it[updatedAt] = java.time.LocalDateTime.now()
            }.value
        }

        val user = transaction {
            UserTable.select { UserTable.id eq newId }
                .map {
                    User(
                        id = it[UserTable.id].value,
                        username = it[UserTable.username],
                        password = it[UserTable.password],
                        createTime = it[UserTable.createdAt].toString()
                    )
                }.first()
        }
        return user
    }

    fun login(request: LoginRequest): User {
        val row = transaction {
            UserTable.select { UserTable.username eq request.username }.singleOrNull()
        } ?: throw RuntimeException("用户不存在")

        val storedHash = row[UserTable.password]
        val match = PasswordUtil.verify(request.password, storedHash)
        if (!match) {
            throw RuntimeException("密码错误")
        }

        return User(
            id = row[UserTable.id].value,
            username = row[UserTable.username],
            password = row[UserTable.password],
            createTime = row[UserTable.createdAt].toString()
        )
    }

    fun getContacts(username: String): List<String> {
        return transaction {
            // 当前用户外的所有用户名
            UserTable.select { UserTable.username neq username }
                .map { it[UserTable.username] }
        }
    }

    fun getUserIdByUsername(username: String): Int? {
        return transaction {
            UserTable.select { UserTable.username eq username }
                .map { it[UserTable.id].value }
                .singleOrNull()
        }
    }

    fun getUsernameById(id: Int): String? {
        return transaction {
            UserTable.select { UserTable.id eq id }
                .map { it[UserTable.username] }
                .singleOrNull()
        }
    }
}
