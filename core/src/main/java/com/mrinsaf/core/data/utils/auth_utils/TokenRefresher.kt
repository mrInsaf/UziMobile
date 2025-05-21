package com.mrinsaf.core.data.utils.auth_utils

import android.content.Context
import com.mrinsaf.core.data.network.dto.network_request.RefreshRequest
import com.mrinsaf.core.data.network.dto.network_responses.RefreshResponse
import com.mrinsaf.core.data.network.data_source.AuthApiService
import com.mrinsaf.core.data.local.data_source.TokenStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenRefresher @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authApiService: AuthApiService
) {
    suspend fun refreshTokens(): RefreshResponse? {
        println("Рефрешу токены")
        val refreshToken = TokenStorage.refreshToken.value ?: return null
        return try {
            val response = authApiService.refreshToken(RefreshRequest(refreshToken))
            TokenStorage.saveAccessToken(context, response.accessToken)
            TokenStorage.saveRefreshToken(context, response.refreshToken)
            response
        } catch (e: Exception) {
            println("Ошибка при обновлении токена $e")
            TokenStorage.clearTokens(context)
            null
        }
    }
}