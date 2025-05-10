package com.mrinsaf.auth.data.repository

import android.content.Context
import com.mrinsaf.auth.data.model.network_requests.RefreshRequest
import com.mrinsaf.auth.data.model.network_responses.RefreshResponse
import com.mrinsaf.core.data.network.AuthApiService
import com.mrinsaf.core.data.repository.local.TokenStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenRefresher @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authApiService: AuthApiService
) {
    suspend fun refreshTokens(): RefreshResponse? {
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