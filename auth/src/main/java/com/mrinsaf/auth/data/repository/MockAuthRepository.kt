package com.mrinsaf.auth.data.repository

import com.mrinsaf.auth.domain.AuthRepository
import com.mrinsaf.core.data.mock.MockDataSource
import com.mrinsaf.core.data.mock.MockUser
import com.mrinsaf.core.data.network.dto.network_request.RegPatientRequest
import com.mrinsaf.core.data.network.dto.network_responses.LoginResponse
import com.mrinsaf.core.data.network.dto.network_responses.RegPatientResponse

class MockAuthRepository(): AuthRepository {
    override suspend fun submitLogin(
        email: String,
        password: String
    ): LoginResponse {
        try {
            val loginResponse = MockDataSource.login(email, password)
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

        MockDataSource.registerUser(newUser)
        return RegPatientResponse(id = userId)
    }
}