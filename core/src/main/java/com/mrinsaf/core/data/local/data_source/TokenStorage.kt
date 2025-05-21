package com.mrinsaf.core.data.local.data_source

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

object TokenStorage {
    private val Context.tokenDataStore by preferencesDataStore(name = "token_data_store").also {
        println("Создается DataStore для файла: token_data_store")
    }

    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")

    private val _accessToken = MutableStateFlow<String?>(null)
    val accessToken: StateFlow<String?> = _accessToken

    private val _refreshToken = MutableStateFlow<String?>(null)
    val refreshToken: StateFlow<String?> = _refreshToken

    fun initialize(context: Context) {
        runBlocking {
            _accessToken.value = context.tokenDataStore.data
                .map { it[ACCESS_TOKEN_KEY] }
                .firstOrNull()

            _refreshToken.value = context.tokenDataStore.data
                .map { it[REFRESH_TOKEN_KEY] }
                .firstOrNull()
        }
    }

    suspend fun saveAccessToken(context: Context, token: String) {
        context.tokenDataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
        }
        _accessToken.value = token // Обновляем StateFlow
    }

    // Сохранение refresh token
    suspend fun saveRefreshToken(context: Context, token: String) {
        context.tokenDataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = token
        }
        _refreshToken.value = token // Обновляем StateFlow
    }

    // Очистка всех токенов
    suspend fun clearTokens(context: Context) {
        context.tokenDataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
        _accessToken.value = null // Сбрасываем StateFlow
        _refreshToken.value = null
    }
}