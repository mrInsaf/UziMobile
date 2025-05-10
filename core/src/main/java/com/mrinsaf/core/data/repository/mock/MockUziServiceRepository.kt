package com.mrinsaf.core.data.repository.mock

import android.net.Uri
import com.mrinsaf.core.data.model.network_responses.NodesSegmentsResponse
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
        TODO("Not yet implemented")
    }

    override suspend fun getImageNodesAndSegments(imageId: String, diagnosticCompleted: Boolean): NodesSegmentsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun downloadUziImage(uziId: String, imageId: String): ResponseBody {
        TODO("Not yet implemented")
    }

//    override suspend fun saveUziImageAndGetCacheUri(uziId: String, imageId: String): Uri {
//        TODO("Not yet implemented")
//    }

    override suspend fun getPatientUzis(patientId: String): List<Uzi> {
        TODO("Not yet implemented")
    }

    override suspend fun getUziNodes(uziId: String): List<Node> {
        TODO("Not yet implemented")
    }

    override suspend fun getUzi(uziId: String): Uzi {
        TODO("Not yet implemented")
    }

    override suspend fun getUziDevices(): List<UziDevice> {
        TODO("Not yet implemented")
    }
}