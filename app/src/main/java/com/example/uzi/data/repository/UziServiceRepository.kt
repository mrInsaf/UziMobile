package com.example.uzi.data.repository

import com.example.uzi.data.models.ReportResponse
import com.example.uzi.data.models.User

interface UziServiceRepository {
    suspend fun checkAuthorisation(): Boolean

    suspend fun submitLogin(
        email: String,
        password: String,
    ): Boolean

    suspend fun submitLogout(): Boolean

    suspend fun getUser(): User

    suspend fun getReportByUziId(uziId: String): ReportResponse
}