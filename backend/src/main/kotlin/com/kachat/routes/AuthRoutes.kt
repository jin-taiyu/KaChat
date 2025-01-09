package com.kachat.routes

import com.kachat.dto.LoginRequest
import com.kachat.dto.RegisterRequest
import com.kachat.service.AuthService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {
    val service = AuthService()

    route("/api/auth") {
        post("/register") {
            try {
                val request = call.receive<RegisterRequest>()
                val user = service.register(request)
                call.respond(mapOf("msg" to "注册成功", "user" to user.username))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        post("/login") {
            try {
                val request = call.receive<LoginRequest>()
                val user = service.login(request)
                call.respond(mapOf("msg" to "登录成功", "user" to user.username))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        get("/contacts/{username}") {
            val username = call.parameters["username"]
            if (username == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "用户名不能为空"))
                return@get
            }

            try {
                val contacts = service.getContacts(username)
                call.respond(contacts)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        get("/userId/{username}") {
            val username = call.parameters["username"]
            if (username == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "用户名不能为空"))
                return@get
            }

            try {
                val userId = service.getUserIdByUsername(username)
                if (userId != null) {
                    call.respond(mapOf("userId" to userId))
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "用户不存在"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        get("/username/{id}") {
            val idParam = call.parameters["id"]
            if (idParam == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "用户ID不能为空"))
                return@get
            }

            val id = idParam.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "无效的用户ID"))
                return@get
            }

            try {
                val username = service.getUsernameById(id)
                if (username != null) {
                    call.respond(mapOf("username" to username))
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "用户不存在"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }
    }
}
