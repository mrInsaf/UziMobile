package com.mrinsaf.core.data.repository

import android.net.Uri
import com.mrinsaf.core.data.models.basic.Node
import com.mrinsaf.core.data.models.networkResponses.NodesSegmentsResponse
import com.mrinsaf.core.data.models.basic.Uzi
import com.mrinsaf.core.data.models.basic.UziImage
import okhttp3.ResponseBody
import com.mrinsaf.core.data.models.networkRequests.RegPatientRequest
import com.mrinsaf.core.data.models.networkResponses.RegPatientResponse

interface UziServiceRepository {
    suspend fun checkAuthorisation(): Boolean

    suspend fun submitLogin(
        email: String,
        password: String,
    ): com.mrinsaf.core.data.models.networkResponses.LoginResponse

    suspend fun refreshToken(): Unit

    suspend fun submitLogout(): Boolean

    suspend fun getUser(): com.mrinsaf.core.data.models.User

    suspend fun createUzi(
        uziUris: Uri, // URI УЗИ файла
        projection: String, // Проекция УЗИ
        patientId: String, // ID пациента
        deviceId: String // ID устройства
    ): String

    suspend fun getUziImages(
        uziId: String // ID УЗИ
    ): List<UziImage>

    suspend fun getImageNodesAndSegments(imageId: String, diagnosticCompleted: Boolean): NodesSegmentsResponse

    suspend fun downloadUziImage(uziId: String, imageId: String): ResponseBody

//    suspend fun saveUziImageAndGetCacheUri(uziId: String, imageId: String): Uri

    suspend fun getPatientUzis(patientId: String): List<Uzi>

    suspend fun getUziNodes(uziId: String): List<Node>

    suspend fun getUzi(uziId: String): Uzi

    suspend fun regPatient(request: RegPatientRequest): RegPatientResponse
}
