package com.example.uzi.network

import com.example.uzi.data.models.networkRequests.LoginRequest
import com.example.uzi.data.models.networkResponses.LoginResponse
import com.example.uzi.data.models.networkResponses.RefreshResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UziApiService {
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Header("token") refreshToken: String // Передаем refresh token в заголовке
    ): RefreshResponse
}