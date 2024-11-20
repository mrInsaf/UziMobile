package com.example.uzi.data.repository

interface UziServiceRepository {
    suspend fun checkAuthorisation(): Boolean

    suspend fun submitLogin(
        email: String,
        password: String,
    ): Boolean

    suspend fun submitLogout(): Boolean
}