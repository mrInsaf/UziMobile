package com.mrinsaf.core.data.repository.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object UserInfoStorage {
    private val Context.userInfoDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_info")

    private val USER_ID_KEY = stringPreferencesKey("user_id")

    suspend fun saveUserId(context: Context, userId: String) {
        context.userInfoDataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
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