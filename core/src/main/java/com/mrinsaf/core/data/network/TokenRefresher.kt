package com.mrinsaf.core.data.network

import android.content.Context
import com.mrinsaf.core.data.models.networkResponses.RefreshResponse
import com.mrinsaf.core.data.repository.local.TokenStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class TokenRefresher @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authApiService: AuthApiService
) {
    suspend fun refreshTokens(): RefreshResponse? {
        val refreshToken = TokenStorage.getRefreshToken(context).firstOrNull() ?: return null
        return try {
            val response = authApiService.refreshToken(refreshToken)
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