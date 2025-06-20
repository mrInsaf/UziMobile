package com.mrinsaf.core.data.network.repository

import android.net.Uri
import com.mrinsaf.core.data.mock.MockAuthDataSource
import com.mrinsaf.core.data.mock.MockUziServiceDataSource
import com.mrinsaf.core.data.network.dto.network_responses.NodesSegmentsResponse
import com.mrinsaf.core.domain.model.User
import com.mrinsaf.core.domain.model.basic.Node
import com.mrinsaf.core.domain.model.basic.Uzi
import com.mrinsaf.core.domain.model.basic.UziDevice
import com.mrinsaf.core.domain.model.basic.UziImage
import com.mrinsaf.core.domain.repository.UziServiceRepository
import okhttp3.ResponseBody

class MockUziServiceRepository(
//    private val context: Context
) : UziServiceRepository {

    override suspend fun getPatient(patientId: String): User {
        TODO("Not yet implemented")
    }

    override suspend fun createUzi(
        uziUris: Uri,
        projection: String,
        patientId: String,
        deviceId: String
    ): String {
        TODO("Not yet implemented")
    }


    override suspend fun getUziImages(uziId: String): List<UziImage> {
        return MockUziServiceDataSource.getUziImages(uziId)
    }

    override suspend fun getImageNodesAndSegments(imageId: String, diagnosticCompleted: Boolean): NodesSegmentsResponse {
        return MockUziServiceDataSource.getImageNodesAndSegments(imageId, diagnosticCompleted)
    }

    override suspend fun downloadUziImage(uziId: String, imageId: String): ResponseBody {
        return MockUziServiceDataSource.downloadUziImage(uziId, imageId)
    }

    override suspend fun getPatientUzis(patientId: String): List<Uzi> {
        return MockUziServiceDataSource.getPatientUzis(patientId)
    }

    override suspend fun getUziNodes(uziId: String): List<Node> {
        return MockUziServiceDataSource.getUziNodes(uziId)
    }

    override suspend fun getUzi(uziId: String): Uzi {
        TODO("Not yet implemented")
    }

    override suspend fun getUziDevices(): List<UziDevice> {
        return MockAuthDataSource.getUziDevices()
    }
}