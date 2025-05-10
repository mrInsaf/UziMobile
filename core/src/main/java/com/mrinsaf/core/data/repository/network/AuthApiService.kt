package com.mrinsaf.core.data.repository.network

import com.mrinsaf.core.data.model.networkRequests.LoginRequest
import com.mrinsaf.core.data.model.networkRequests.RefreshRequest
import com.mrinsaf.core.data.model.networkRequests.RegPatientRequest
import com.mrinsaf.core.data.model.networkResponses.LoginResponse
import com.mrinsaf.core.data.model.networkResponses.RefreshResponse
import com.mrinsaf.core.data.model.networkResponses.RegPatientResponse
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
        @Body refreshToken: RefreshRequest
    ): RefreshResponse
}