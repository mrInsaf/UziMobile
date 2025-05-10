package com.mrinsaf.core.domain.repository

import com.mrinsaf.core.data.model.networkRequests.RegPatientRequest
import com.mrinsaf.core.data.model.networkResponses.LoginResponse
import com.mrinsaf.core.data.model.networkResponses.RegPatientResponse
import com.mrinsaf.core.domain.model.User

interface AuthRepository {
    suspend fun submitLogin(
        email: String,
        password: String,
    ): LoginResponse

    suspend fun refreshToken(): Unit

    suspend fun registerPatient(request: RegPatientRequest): RegPatientResponse
}