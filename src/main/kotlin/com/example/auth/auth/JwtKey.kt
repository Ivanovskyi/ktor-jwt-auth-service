package com.example.auth.auth

import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

data class JwtKey(
    val id: String,
    val publicKey: RSAPublicKey,
    val privateKey: RSAPrivateKey
)
