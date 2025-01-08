package com.example.uzi.data.repository

import android.net.Uri
import com.example.uzi.data.models.networkResponses.LoginResponse
import com.example.uzi.data.models.networkResponses.ReportResponse
import com.example.uzi.data.models.User
import com.example.uzi.data.models.networkResponses.NodesSegmentsResponse
import com.example.uzi.data.models.networkResponses.UziImage
import okhttp3.ResponseBody

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
        uziUris: List<Uri>, // URI УЗИ файла
        projection: String, // Проекция УЗИ
        patientId: String, // ID пациента
        deviceId: String // ID устройства
    ): String

    suspend fun getUziById(uziId: String): ReportResponse

    suspend fun getUziImages(
        uziId: String // ID УЗИ
    ): List<UziImage>

    suspend fun getImageNodesAndSegments(imageId: String): NodesSegmentsResponse

    suspend fun downloadUziImage(uziId: String, imageId: String): ResponseBody

    suspend fun saveUziImageAndGetCacheUri(uziId: String, imageId: String): Uri
}