package com.mrinsaf.auth.data.repository

import android.content.Context
import com.mrinsaf.auth.domain.AuthRepository
import com.mrinsaf.core.data.network.dto.network_request.LoginRequest
import com.mrinsaf.core.data.network.dto.network_request.RegPatientRequest
import com.mrinsaf.core.data.network.dto.network_responses.LoginResponse
import com.mrinsaf.core.data.network.dto.network_responses.RegPatientResponse
import com.mrinsaf.core.data.network.data_source.AuthApiService
import com.mrinsaf.core.data.local.data_source.TokenStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authApiService: AuthApiService,
): AuthRepository {
    override suspend fun submitLogin(email: String, password: String): LoginResponse {
        val loginResponse = authApiService.login(
            request = LoginRequest(
                email = email,
                password = password
            )
        )
        loginResponse.accessToken.let {
            TokenStorage.saveAccessToken(context, it)
        }
        loginResponse.refreshToken.let {
            TokenStorage.saveRefreshToken(context, it)
        }
        return loginResponse
    }


    override suspend fun registerPatient(request: RegPatientRequest): RegPatientResponse {
        return authApiService.regPatient(request).body()!!
    }
}