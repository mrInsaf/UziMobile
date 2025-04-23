package com.mrinsaf.auth.domain

interface AuthRepository {
    suspend fun checkAuthState()
}

