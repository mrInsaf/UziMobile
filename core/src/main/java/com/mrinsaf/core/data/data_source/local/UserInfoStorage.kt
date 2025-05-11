package com.mrinsaf.core.data.data_source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mrinsaf.core.data.utils.local.JwtDecoder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object UserInfoStorage {
    private val Context.userInfoDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_info")

    private val USER_ID_KEY = stringPreferencesKey("user_id")

    suspend fun saveUserId(context: Context, userId: String) {
        context.userInfoDataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    suspend fun saveDecodedUserIdFromToken(context: Context): String? {
        val accessToken = TokenStorage.accessToken.value
        accessToken?.let { token ->
            try {
                val decodedUserId = JwtDecoder.getTokenId(token)
                println("decoded userId: $decodedUserId")
                saveUserId(context, decodedUserId)
                return decodedUserId
            }
            catch (e: Exception) {
                println("Не удалось декодировать или сохранить user id: $e")
                return null
            }
        }
        return null
    }

    fun getUserId(context: Context): Flow<String> {
        return context.applicationContext.userInfoDataStore.data
            .map { preferences -> preferences[USER_ID_KEY] ?: "" }
    }

    suspend fun clearUserId(context: Context) {
        context.userInfoDataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
        }
    }
}