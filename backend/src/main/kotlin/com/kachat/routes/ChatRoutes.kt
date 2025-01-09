package com.kachat.routes

import com.kachat.dto.MessageDto
import com.kachat.service.ChatService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.chatRoutes() {
    val service = ChatService()

    route("/api/chat") {
        post("/message") {
            try {
                val dto = call.receive<MessageDto>()
                service.saveMessage(dto)
                call.respond(mapOf("msg" to "发送成功"))
            } catch (e: RuntimeException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }

        get("/messages") {
            val roomType = call.request.queryParameters["roomType"] ?: "PUBLIC"
            val senderUsername = call.request.queryParameters["sender"]
            val recipientUsername = call.request.queryParameters["recipient"]

            try {
                val msgs = service.getMessages(roomType, senderUsername, recipientUsername)
                call.respond(msgs)
            } catch (e: RuntimeException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            }
        }
    }
}
