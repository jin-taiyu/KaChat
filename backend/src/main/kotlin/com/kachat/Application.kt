package com.kachat

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.kachat.plugins.*
import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.micrometer.prometheus.PrometheusConfig
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.websocket.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import com.kachat.db.UserTable
import com.kachat.db.MessageTable

fun main() {
    embeddedServer(Netty) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    installPlugins()
    configureDatabase()
    configureRouting()
}

fun Application.installPlugins() {
    configureSerialization()
    configureSecurity()
    configureMonitoring()
    configureCors()
    configureWebSockets()
}

fun Application.configureWebSockets() {
    install(WebSockets)
}

fun Application.configureMonitoring() {
    val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    install(MicrometerMetrics) {
        registry = appMicrometerRegistry
    }
}

fun Application.configureCors() {
    install(CORS) {
        anyHost()
        allowCredentials = true
        allowNonSimpleContentTypes = true
    }
}

fun Application.configureDatabase() {
    val dbUrl = environment.config.propertyOrNull("database.url")?.getString()
    val dbUser = environment.config.propertyOrNull("database.user")?.getString()
    val dbPassword = environment.config.propertyOrNull("database.password")?.getString()
    val dbDriver = environment.config.propertyOrNull("database.driver")?.getString()

    Database.connect(
        url = dbUrl ?: "jdbc:postgresql://localhost:5432/kachat_db",
        driver = dbDriver ?: "org.postgresql.Driver",
        user = dbUser ?: "postgres",
        password = dbPassword ?: "postgres"
    )

    // 创建表
    transaction {
        SchemaUtils.create(UserTable, MessageTable)
    }
}
