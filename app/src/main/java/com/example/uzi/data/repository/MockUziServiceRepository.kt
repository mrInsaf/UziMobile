package com.example.uzi.data.repository

import android.content.Context
import android.net.Uri
import com.example.uzi.data.mock.MockAuthData
import com.example.uzi.data.models.User
import com.example.uzi.data.models.networkResponses.LoginResponse
import com.example.uzi.data.models.networkResponses.NodesSegmentsResponse
import com.example.uzi.data.models.networkResponses.ReportResponse
import com.example.uzi.data.models.networkResponses.Uzi
import com.example.uzi.data.models.networkResponses.UziImage
import com.example.uzi.data.models.networkResponses.UziNodesResponse
import com.example.uzi.data.repository.local.TokenStorage
import kotlinx.coroutines.delay
import okhttp3.ResponseBody

class MockUziServiceRepository(
//    private val context: Context
) : UziServiceRepository {

    private val mockAuthData = MockAuthData()

    override suspend fun checkAuthorisation(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun submitLogin(email: String, password: String): LoginResponse {
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

    override suspend fun getUser(): User {
        TODO("Not yet implemented")
    }

    override suspend fun createUzi(
        uziUris: List<Uri>,
        projection: String,
        patientId: String,
        deviceId: String
    ): String {
        TODO("Not yet implemented")
    }

    override suspend fun getUziById(uziId: String): ReportResponse {
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

    override suspend fun saveUziImageAndGetCacheUri(uziId: String, imageId: String): Uri {
        TODO("Not yet implemented")
    }

    override suspend fun downloadUziFile(uziId: String): ResponseBody {
        TODO("Not yet implemented")
    }

    override suspend fun saveUziFileAndGetCacheUri(uziId: String, responseBody: ResponseBody): Uri {
        TODO("Not yet implemented")
    }

    override suspend fun getUziList(patientId: String): List<Uzi> {
        TODO("Not yet implemented")
    }

    override suspend fun getUziNodes(uziId: String): UziNodesResponse {
        TODO("Not yet implemented")
    }
}
