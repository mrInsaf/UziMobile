package com.mrinsaf.core.data.repository.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date


object TokenStorage {
    private val Context.tokenDataStore by preferencesDataStore(name = "token_data_store").also {
        println("Создается DataStore для файла: token_data_store")
    }

    init {
        println("Создаю TokenStorage")
    }

    // Ключи для хранения токенов
    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")

    // Сохранение access token
    suspend fun saveAccessToken(context: Context, token: String) {
        context.tokenDataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
        }
    }

    // Сохранение refresh token
    suspend fun saveRefreshToken(context: Context, token: String) {
        context.tokenDataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = token
        }
    }

    // Извлечение access token
    fun getAccessToken(context: Context): Flow<String?> {
        try {
            return context.tokenDataStore.data
                .map { preferences ->
                    preferences[ACCESS_TOKEN_KEY]
                }
        } catch (e: Exception) {
            println(e)
            throw e
        }
    }

    // Извлечение refresh token
    fun getRefreshToken(context: Context): Flow<String?> {
        return context.tokenDataStore.data
            .map { preferences ->
                preferences[REFRESH_TOKEN_KEY]
            }
    }

    fun validateToken(token: String): Boolean {
        return try {
            // Декодируем токен
            val jwt = JWT.decode(token)

            // Проверяем время истечения
            val expiresAt = jwt.expiresAt
            if (expiresAt != null && expiresAt.after(Date())) {
                println("Токен валиден до: $expiresAt")
                true
            } else {
                println("Токен истек!")
                false
            }
        } catch (e: JWTDecodeException) {
            println("Ошибка декодирования токена: ${e.message}")
            false
        }
    }

    // Очистка всех токенов
    suspend fun clearTokens(context: Context) {
        context.tokenDataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }
}
