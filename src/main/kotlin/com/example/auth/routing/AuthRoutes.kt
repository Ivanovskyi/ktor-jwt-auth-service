package com.example.auth.routing

import com.example.auth.auth.JwtService
import com.example.auth.config.AppConfig
import com.example.auth.model.LoginRequest
import com.example.auth.model.TokenResponse
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes(
    config: AppConfig,
    jwtService: JwtService
) {
    post("/login") {
        val request = try {
            call.receive<LoginRequest>()
        } catch (_: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Invalid request body")
            )
            return@post
        }

        if (
            request.username != config.username ||
            request.password != config.password
        ) {
            call.respond(
                HttpStatusCode.Unauthorized,
                mapOf("error" to "Invalid credentials")
            )
            return@post
        }

        val token = jwtService.createToken(request.username)

        call.respond(TokenResponse(token))
    }
}
