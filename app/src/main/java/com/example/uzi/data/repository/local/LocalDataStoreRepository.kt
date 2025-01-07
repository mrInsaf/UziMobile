package com.example.uzi.data.repository.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

class LocalDataStoreRepository(private val dataStore: DataStore<Preferences>) : LocalDataRepository {
    private object PreferencesKeys {
        val UZI_IDS = stringPreferencesKey("uzi_ids")
    }

    override suspend fun saveUziId(uziId: String) {
        val currentIds = getUziIds().toMutableSet()
        currentIds.add(uziId)
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.UZI_IDS] = currentIds.joinToString(",")
        }
    }

    override suspend fun getUziIds(): List<String> {
        val ids = dataStore.data.first()[PreferencesKeys.UZI_IDS]
        return ids?.split(",") ?: emptyList()
    }
}
