package com.mrinsaf.core.domain.repository

interface LocalDataRepository {
    suspend fun saveUziId(uziId: String)
    suspend fun getUziIds(): List<String>
}