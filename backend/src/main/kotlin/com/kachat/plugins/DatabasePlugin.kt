package com.kachat.plugins

import com.kachat.db.MessageTable
import com.kachat.db.UserTable
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    // 从 application.conf 读取配置
    val dbUrl = environment.config.propertyOrNull("database.url")?.getString() ?: "jdbc:postgresql://localhost:5432/kachat_db"
    val dbUser = environment.config.propertyOrNull("database.user")?.getString() ?: "postgres"
    val dbPassword = environment.config.propertyOrNull("database.password")?.getString() ?: "postgres"
    val driver = environment.config.propertyOrNull("database.driver")?.getString() ?: "org.postgresql.Driver"

    Database.connect(
        url = dbUrl,
        driver = driver,
        user = dbUser,
        password = dbPassword
    )

    transaction {
        SchemaUtils.create(UserTable, MessageTable)
    }
}
