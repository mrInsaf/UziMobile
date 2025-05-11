package com.mrinsaf.auth.data.repository

import android.content.Context
import com.mrinsaf.auth.domain.AuthRepository
import com.mrinsaf.core.data.model.network_request.LoginRequest
import com.mrinsaf.core.data.model.network_request.RefreshRequest
import com.mrinsaf.core.data.model.network_request.RegPatientRequest
import com.mrinsaf.core.data.model.network_responses.LoginResponse
import com.mrinsaf.core.data.model.network_responses.RegPatientResponse
import com.mrinsaf.core.data.data_source.network.AuthApiService
import com.mrinsaf.core.data.data_source.local.TokenStorage
import com.mrinsaf.core.data.repository.network.NetworkUziServiceRepository.TokenNotFoundException
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

    override suspend fun refreshToken() {
        println("Рефрешу токен")
        val refreshToken = TokenStorage.refreshToken.value

        if (refreshToken != null) {
            val response = authApiService.refreshToken(RefreshRequest(refreshToken))

            response.accessToken.let {
                TokenStorage.saveAccessToken(context, it)
            }

            response.refreshToken.let {
                TokenStorage.saveRefreshToken(context, it)
            }
        } else {
            throw TokenNotFoundException("Refresh token not found")
        }
    }


    override suspend fun registerPatient(request: RegPatientRequest): RegPatientResponse {
        return authApiService.regPatient(request).body()!!
    }
}