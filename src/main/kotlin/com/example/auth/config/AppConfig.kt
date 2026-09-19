package com.example.auth.config

import io.ktor.server.config.*

data class AppConfig(
    val issuer: String,
    val audience: String,
    val realm: String,
    val username: String,
    val password: String,
    val accessTokenTtlMs: Long
) {
    companion object {
        fun from(config: ApplicationConfig): AppConfig {
            return AppConfig(
                issuer = config.property("auth.issuer").getString(),
                audience = config.property("auth.audience").getString(),
                realm = config.property("auth.realm").getString(),
                username = config.property("auth.username").getString(),
                password = config.property("auth.password").getString(),
                accessTokenTtlMs =
                    config.property("auth.accessTokenTtlMs").getString().toLong()
            )
        }
    }
}
