package com.kachat.routes

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.chatWebSocketRoutes() {
    webSocket("/ws/chat") {
        send("你已连接到Ktor WebSocket聊天！")

        // 接收&广播
        incoming.consumeEach { frame ->
            if (frame is Frame.Text) {
                val receivedText = frame.readText()
                send("服务端已收到：$receivedText")
            }
        }
    }
}
