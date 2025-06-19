package com.mrinsaf.core.data.utils.local

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

object JwtEncoder {
    private const val DEFAULT_EXPIRATION_MS = 3600_000L // 1 час

    fun generateToken(
        userId: String,
        expirationMs: Long = DEFAULT_EXPIRATION_MS
    ): String {
        val expiresAt = Date(System.currentTimeMillis() + expirationMs)

        return JWT.create()
            .withClaim("id", userId)
            .withExpiresAt(expiresAt)
            .sign(Algorithm.none())
    }
}