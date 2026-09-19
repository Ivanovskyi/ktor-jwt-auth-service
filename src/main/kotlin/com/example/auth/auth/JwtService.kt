package com.example.auth.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.auth.config.AppConfig
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.Date

class JwtService(
    private val config: AppConfig
) {
    private val key: JwtKey = generateKey()

    private val algorithm =
        Algorithm.RSA256(key.publicKey, key.privateKey)

    fun createToken(username: String): String {
        val now = System.currentTimeMillis()

        return JWT.create()
            .withIssuer(config.issuer)
            .withAudience(config.audience)
            .withSubject(username)
            .withKeyId(key.id)
            .withIssuedAt(Date(now))
            .withExpiresAt(
                Date(now + config.accessTokenTtlMs)
            )
            .sign(algorithm)
    }

    fun publicKey(): RSAPublicKey = key.publicKey

    fun keyId(): String = key.id

    private fun generateKey(): JwtKey {
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(2048)

        val pair = generator.generateKeyPair()

        return JwtKey(
            id = "kid-2026-v1",
            publicKey = pair.public as RSAPublicKey,
            privateKey = pair.private as RSAPrivateKey
        )
    }
}
