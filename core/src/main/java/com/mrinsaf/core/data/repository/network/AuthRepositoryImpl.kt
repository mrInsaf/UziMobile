package com.mrinsaf.core.data.repository.network

import android.content.Context
import com.mrinsaf.core.data.model.networkRequests.LoginRequest
import com.mrinsaf.core.data.model.networkRequests.RefreshRequest
import com.mrinsaf.core.data.model.networkRequests.RegPatientRequest
import com.mrinsaf.core.data.model.networkResponses.LoginResponse
import com.mrinsaf.core.data.model.networkResponses.RegPatientResponse
import com.mrinsaf.core.data.repository.local.TokenStorage
import com.mrinsaf.core.data.repository.network.NetworkUziServiceRepository.TokenNotFoundException
import com.mrinsaf.core.domain.repository.AuthRepository
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