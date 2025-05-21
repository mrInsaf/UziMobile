package com.mrinsaf.auth.domain

import com.mrinsaf.core.data.network.dto.network_request.RegPatientRequest
import com.mrinsaf.core.data.network.dto.network_responses.LoginResponse
import com.mrinsaf.core.data.network.dto.network_responses.RegPatientResponse

interface AuthRepository {
    suspend fun submitLogin(
        email: String,
        password: String,
    ): LoginResponse

    suspend fun refreshToken(): Unit

    suspend fun registerPatient(request: RegPatientRequest): RegPatientResponse
}