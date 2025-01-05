package com.example.uzi.network

import com.example.uzi.data.models.networkRequests.LoginRequest
import com.example.uzi.data.models.networkResponses.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UziApiService {
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse
}