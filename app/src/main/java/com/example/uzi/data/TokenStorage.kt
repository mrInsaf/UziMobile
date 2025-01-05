package com.example.uzi.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


object TokenStorage {
    private val Context.tokenDataStore by preferencesDataStore(name = "token_data_store")

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
        return context.tokenDataStore.data
            .map { preferences ->
                preferences[ACCESS_TOKEN_KEY]
            }
    }

    // Извлечение refresh token
    fun getRefreshToken(context: Context): Flow<String?> {
        return context.tokenDataStore.data
            .map { preferences ->
                preferences[REFRESH_TOKEN_KEY]
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
