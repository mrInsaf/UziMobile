package com.mrinsaf.auth.domain

import com.mrinsaf.auth.data.model.network_requests.RegPatientRequest
import com.mrinsaf.auth.data.model.network_responses.LoginResponse
import com.mrinsaf.auth.data.model.network_responses.RegPatientResponse

interface AuthRepository {
    suspend fun submitLogin(
        email: String,
        password: String,
    ): LoginResponse

    suspend fun refreshToken(): Unit

    suspend fun registerPatient(request: RegPatientRequest): RegPatientResponse
}