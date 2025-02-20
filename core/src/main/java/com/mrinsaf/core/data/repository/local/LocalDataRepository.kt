package com.mrinsaf.core.data.repository.local

interface LocalDataRepository {
    suspend fun saveUziId(uziId: String)
    suspend fun getUziIds(): List<String>
}
