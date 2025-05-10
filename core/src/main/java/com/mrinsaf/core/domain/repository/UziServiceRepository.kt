package com.mrinsaf.core.domain.repository

import android.net.Uri
import com.mrinsaf.core.data.model.network_responses.NodesSegmentsResponse
import com.mrinsaf.core.domain.model.User
import com.mrinsaf.core.domain.model.basic.Node
import com.mrinsaf.core.domain.model.basic.Uzi
import com.mrinsaf.core.domain.model.basic.UziDevice
import com.mrinsaf.core.domain.model.basic.UziImage
import okhttp3.ResponseBody

interface UziServiceRepository {
    suspend fun getPatient(patientId: String): User

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

    suspend fun getPatientUzis(patientId: String): List<Uzi>

    suspend fun getUziNodes(uziId: String): List<Node>

    suspend fun getUzi(uziId: String): Uzi?

    suspend fun getUziDevices(): List<UziDevice>
}