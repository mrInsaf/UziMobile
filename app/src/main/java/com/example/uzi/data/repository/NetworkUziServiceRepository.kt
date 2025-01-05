package com.example.uzi.data.repository

import android.content.Context
import android.net.Uri
import com.example.uzi.data.TokenStorage
import com.example.uzi.data.models.networkResponses.LoginResponse
import com.example.uzi.data.models.networkResponses.ReportResponse
import com.example.uzi.data.models.User
import com.example.uzi.data.models.networkRequests.LoginRequest
import com.example.uzi.network.UziApiService
import kotlinx.coroutines.flow.first

class NetworkUziServiceRepository(
    private val uziApiService: UziApiService,
    private val context: Context
): UziServiceRepository {
    override suspend fun checkAuthorisation(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun submitLogin(email: String, password: String): LoginResponse {
        val loginResponse = uziApiService.login(
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
        val refreshToken = TokenStorage.getRefreshToken(context).first()

        if (refreshToken != null) {
            val response = uziApiService.refreshToken(refreshToken)

            response.access_key.let {
                TokenStorage.saveAccessToken(context, it)
            }
            response.refresh_key.let {
                TokenStorage.saveRefreshToken(context, it)
            }
        } else {
            throw TokenNotFoundException("Refresh token not found")
        }
    }

    override suspend fun submitLogout(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(): User {
        TODO("Not yet implemented")
    }

    override suspend fun createUzi(
        patientId: String,
        imageUris: List<Uri>,
        dateOfAdmission: String,
        clinicName: String
    ): String {
        TODO("Not yet implemented")
    }

    override suspend fun getUziById(uziId: String): ReportResponse {
        TODO("Not yet implemented")
    }

    class TokenNotFoundException(message: String) : Exception(message)
}

