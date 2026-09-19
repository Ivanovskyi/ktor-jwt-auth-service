package com.example.auth.routing

import com.example.auth.auth.JwtService
import com.example.auth.model.JwkKey
import com.example.auth.model.JwksResponse
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.math.BigInteger
import java.util.Base64

fun Route.jwksRoutes(jwtService: JwtService) {
    get("/.well-known/jwks.json") {
        val key = jwtService.publicKey()
        val encoder = Base64.getUrlEncoder().withoutPadding()

        val jwk = JwkKey(
            kty = "RSA",
            kid = jwtService.keyId(),
            alg = "RS256",
            use = "sig",
            n = encoder.encodeToString(
                key.modulus.toUnsignedBytes()
            ),
            e = encoder.encodeToString(
                key.publicExponent.toUnsignedBytes()
            )
        )

        call.respond(
            JwksResponse(
                keys = listOf(jwk)
            )
        )
    }
}

private fun BigInteger.toUnsignedBytes(): ByteArray {
    val bytes = toByteArray()

    return if (bytes.size > 1 && bytes[0] == 0.toByte()) {
        bytes.copyOfRange(1, bytes.size)
    } else {
        bytes
    }
}
