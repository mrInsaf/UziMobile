package com.mrinsaf.core.data.network.data_source

import com.mrinsaf.core.data.network.dto.network_request.LoginRequest
import com.mrinsaf.core.data.network.dto.network_request.RefreshRequest
import com.mrinsaf.core.data.network.dto.network_request.RegPatientRequest
import com.mrinsaf.core.data.network.dto.network_responses.LoginResponse
import com.mrinsaf.core.data.network.dto.network_responses.RefreshResponse
import com.mrinsaf.core.data.network.dto.network_responses.RegPatientResponse
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