package com.example.uzi.data.repository

import android.net.Uri
import com.example.uzi.data.models.networkResponses.LoginResponse
import com.example.uzi.data.models.networkResponses.ReportResponse
import com.example.uzi.data.models.User
import com.example.uzi.data.models.networkRequests.LoginRequest
import com.example.uzi.network.UziApiService

class NetworkUziServiceRepository(
    private val uziApiService: UziApiService,
): UziServiceRepository {
    override suspend fun checkAuthorisation(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun submitLogin(email: String, password: String): LoginResponse = uziApiService.login(
        request = LoginRequest(
            email = email,
            password = password
        )
    )

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
}