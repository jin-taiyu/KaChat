package com.kachat.plugins

import com.kachat.routes.authRoutes
import com.kachat.routes.chatRoutes
import com.kachat.routes.chatWebSocketRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authRoutes()
        chatRoutes()
        chatWebSocketRoutes()
    }
}
