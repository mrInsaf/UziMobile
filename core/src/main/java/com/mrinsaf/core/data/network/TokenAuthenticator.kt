package com.mrinsaf.core.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenRefresher: TokenRefresher
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? = runBlocking(Dispatchers.IO) {
        if (response.code !in listOf(401, 403, 500)) return@runBlocking null

        val isTokenError = if (response.code == 500) {
            response.peekBody(Long.MAX_VALUE).string().contains("token is expired", ignoreCase = true)
        } else {
            true
        }

        if (!isTokenError) return@runBlocking null

        val newTokens = tokenRefresher.refreshTokens() ?: return@runBlocking null

        return@runBlocking response.request.newBuilder()
            .header("Authorization", "Bearer ${newTokens.accessToken}")
            .build()
    }
}