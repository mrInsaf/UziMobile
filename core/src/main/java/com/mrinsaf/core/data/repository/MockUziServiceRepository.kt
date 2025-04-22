package com.mrinsaf.core.data.repository

import android.net.Uri
import com.mrinsaf.core.data.mock.MockAuthData
import com.mrinsaf.core.data.models.basic.Node
import com.mrinsaf.core.data.models.networkResponses.NodesSegmentsResponse
import com.mrinsaf.core.data.models.basic.Uzi
import com.mrinsaf.core.data.models.basic.UziDevice
import com.mrinsaf.core.data.models.basic.UziImage
import com.mrinsaf.core.data.models.networkRequests.RegPatientRequest
import com.mrinsaf.core.data.models.networkResponses.RegPatientResponse
import okhttp3.ResponseBody

class MockUziServiceRepository(
//    private val context: Context
) : UziServiceRepository {

    private val mockAuthData = MockAuthData()

    override suspend fun checkAuthorisation(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun submitLogin(email: String, password: String): com.mrinsaf.core.data.models.networkResponses.LoginResponse {
//        return if (email == mockAuthData.email && password == mockAuthData.password) {
//            LoginResponse(
//                accessToken = "mock_access_token_123",
//                refreshToken = "mock_refresh_token_456"
//            ).also {
//                TokenStorage.saveAccessToken(context, it.accessToken)
//                TokenStorage.saveRefreshToken(context, it.refreshToken)
//            }
//        } else {
//            throw IllegalArgumentException("Invalid email or password")
//        }
        TODO("Not yet implemented")
    }

    override suspend fun refreshToken() {
        TODO("Not yet implemented")
    }

    override suspend fun submitLogout(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(): com.mrinsaf.core.data.models.User {
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

    override suspend fun regPatient(request: RegPatientRequest): RegPatientResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getUziDevices(): List<UziDevice> {
        TODO("Not yet implemented")
    }
}
