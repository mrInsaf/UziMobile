package com.mrinsaf.core

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.mrinsaf.core.data.repository.local.TokenStorage
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

class TokenStorageTest {

    private lateinit var validToken: String
    private lateinit var expiredToken: String
    private lateinit var invalidToken: String

    @Before
    fun setUp() {
        // Генерируем валидный токен с временем истечения +1 час
        validToken = JWT.create()
            .withSubject("user123")
            .withExpiresAt(Date(System.currentTimeMillis() + 3600 * 1000))
            .sign(Algorithm.HMAC256("secret"))

        // Генерируем истекший токен
        expiredToken = JWT.create()
            .withSubject("user123")
            .withExpiresAt(Date(System.currentTimeMillis() - 3600 * 1000)) // Время в прошлом
            .sign(Algorithm.HMAC256("secret"))

        // Невалидный токен (неправильный формат)
        invalidToken = "invalid.token.format"
    }

    @Test
    fun `validateToken returns true for valid token`() {
        val isValid = TokenStorage.validateToken(validToken)
        assertTrue(isValid)
    }

    @Test
    fun `validateToken returns false for expired token`() {
        val isValid = TokenStorage.validateToken(expiredToken)
        assertFalse(isValid)
    }

    @Test
    fun `validateToken returns false for invalid token format`() {
        val isValid = TokenStorage.validateToken(invalidToken)
        assertFalse(isValid)
    }

    @Test
    fun `validateToken throws exception for malformed token`() {
        val malformedToken = "malformed.token.without.signature"
        val isValid = TokenStorage.validateToken(malformedToken)

        assertFalse(isValid)
    }
}