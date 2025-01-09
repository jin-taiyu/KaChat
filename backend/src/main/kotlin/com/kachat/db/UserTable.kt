package com.kachat.db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object UserTable : IntIdTable("users") {
    val username = varchar("username", 50).uniqueIndex()
    val password = varchar("password", 100)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}
