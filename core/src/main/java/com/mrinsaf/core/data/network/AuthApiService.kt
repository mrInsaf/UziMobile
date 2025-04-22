package com.mrinsaf.core.data.network

import com.mrinsaf.core.data.models.networkRequests.LoginRequest
import com.mrinsaf.core.data.models.networkRequests.RegPatientRequest
import com.mrinsaf.core.data.models.networkResponses.LoginResponse
import com.mrinsaf.core.data.models.networkResponses.RefreshResponse
import com.mrinsaf.core.data.models.networkResponses.RegPatientResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("reg/patient")
    suspend fun regPatient(
        @Body request: RegPatientRequest
    ): Response<RegPatientResponse>

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("refresh")
    suspend fun refreshToken(
        @Body refreshToken: String
    ): RefreshResponse
}