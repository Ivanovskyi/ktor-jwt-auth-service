package com.example.auth

import com.example.auth.auth.JwtService
import com.example.auth.config.AppConfig
import com.example.auth.routing.authRoutes
import com.example.auth.routing.jwksRoutes
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val error: String)

fun Application.module() {
    val config = AppConfig.from(environment.config)
    val jwtService = JwtService(config)

    install(ContentNegotiation) {
        json()
    }

    install(CallLogging)

    install(StatusPages) {
        exception<Throwable> { 
            call, _ -> call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Internal server error"))
        }
    }

    routing {
        authRoutes(config, jwtService)
        jwksRoutes(jwtService)

        get("/health") {
            call.respond(HttpStatusCode.OK, mapOf("status" to "UP"))
        }
    }
}
