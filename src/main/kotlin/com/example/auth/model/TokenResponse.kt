package com.example.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val accessToken: String,
    val tokenType: String = "Bearer"
)
