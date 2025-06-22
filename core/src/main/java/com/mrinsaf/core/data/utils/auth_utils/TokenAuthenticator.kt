package com.mrinsaf.core.data.utils.auth_utils

import com.mrinsaf.core.presentation.event.event_bus.AuthEventBus
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
        if (response.code != 401) return@runBlocking null

        println("Внимание! Пользователь не авторизован")
        val newTokens = tokenRefresher.refreshTokens()
        if (newTokens == null) {
            println("Токены не обновлены, требуется авторизация")
            AuthEventBus.emitAuthRequired()
            return@runBlocking null
        }

        println("Получил новые токены: $newTokens")
        return@runBlocking response.request.newBuilder()
            .header("Authorization", "Bearer ${newTokens.accessToken}")
            .build()
    }
}