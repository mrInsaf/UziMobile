package com.example.uzi.data.repository

import android.net.Uri
import com.example.uzi.data.models.networkResponses.LoginResponse
import com.example.uzi.data.models.networkResponses.ReportResponse
import com.example.uzi.data.models.User

interface UziServiceRepository {
    suspend fun checkAuthorisation(): Boolean

    suspend fun submitLogin(
        email: String,
        password: String,
    ): LoginResponse

    suspend fun refreshToken(): Unit

    suspend fun submitLogout(): Boolean

    suspend fun getUser(): User

    suspend fun createUzi(
        patientId: String,
        imageUris: List<Uri>,
        dateOfAdmission: String,
        clinicName: String,
    ): String

    suspend fun getUziById(uziId: String): ReportResponse
}