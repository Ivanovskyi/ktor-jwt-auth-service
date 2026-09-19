package com.example.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class JwkKey(
    val kty: String,
    val kid: String,
    val alg: String,
    val use: String,
    val n: String,
    val e: String
)

@Serializable
data class JwksResponse(
    val keys: List<JwkKey>
)
