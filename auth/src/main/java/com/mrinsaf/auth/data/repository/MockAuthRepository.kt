package com.mrinsaf.auth.data.repository

import android.content.Context
import com.mrinsaf.auth.domain.AuthRepository
import com.mrinsaf.core.data.local.data_source.TokenStorage
import com.mrinsaf.core.data.local.data_source.UserInfoStorage
import com.mrinsaf.core.data.mock.MockAuthDataSource
import com.mrinsaf.core.data.mock.MockUser
import com.mrinsaf.core.data.mock.MockUziServiceDataSource
import com.mrinsaf.core.data.network.dto.network_request.RegPatientRequest
import com.mrinsaf.core.data.network.dto.network_responses.LoginResponse
import com.mrinsaf.core.data.network.dto.network_responses.RegPatientResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlin.contracts.contract

class MockAuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
): AuthRepository {
    override suspend fun submitLogin(
        email: String,
        password: String
    ): LoginResponse {
        try {
            val loginResponse = MockAuthDataSource.login(email, password)

            loginResponse.accessToken.let {
                TokenStorage.saveAccessToken(context, it)
                println("access token: $it")
            }
            loginResponse.refreshToken.let {
                TokenStorage.saveRefreshToken(context, it)
            }

            return loginResponse
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun registerPatient(request: RegPatientRequest): RegPatientResponse {
        val userId = "${System.currentTimeMillis()}"
        val newUser = MockUser(
            userId = userId,
            userName = request.fullname,
            email = request.email,
            password = request.password
        )

        MockAuthDataSource.registerUser(newUser)
        return RegPatientResponse(id = userId)
    }
}