package com.example.uzi.data.repository

import android.net.Uri
import com.example.uzi.data.models.User
import com.example.uzi.data.models.networkResponses.LoginResponse
import com.example.uzi.data.models.networkResponses.NodesSegmentsResponse
import com.example.uzi.data.models.networkResponses.ReportResponse
import com.example.uzi.data.models.networkResponses.UziImage
import okhttp3.ResponseBody

//
//import android.net.Uri
//import com.example.uzi.data.mock.MockAuthData
//import com.example.uzi.data.mock.MockUziReport
//import com.example.uzi.data.models.networkResponses.Formation
//import com.example.uzi.data.models.networkResponses.Image
//import com.example.uzi.data.models.networkResponses.LoginResponse
//import com.example.uzi.data.models.networkResponses.ReportResponse
//import com.example.uzi.data.models.networkResponses.SectorPoint
//import com.example.uzi.data.models.networkResponses.Segment
//import com.example.uzi.data.models.networkResponses.Tirads
//import com.example.uzi.data.models.User
//import com.example.uzi.data.models.networkResponses.Uzi
//import kotlinx.coroutines.delay
//
class MockUziServiceRepository : UziServiceRepository {
    //    private val authData = MockAuthData()
//    private val mockUziReports: MutableList<MockUziReport> = mutableListOf()
//    private val mockUziReportResponses: MutableList<ReportResponse> = mutableListOf()
//
//    override suspend fun checkAuthorisation(): Boolean {
//        delay(100) // Имитация задержки
//        return authData.isAuthorised
//    }
//
//    override suspend fun submitLogin(
//        email: String,
//        password: String
//    ): LoginResponse {
//        delay(100) // Симуляция сетевого вызова
//
//        if (email == authData.email && password == authData.password) {
//            return LoginResponse(
//                accessToken = "mock_access_token",
//                refreshToken = "mock_refresh_token"
//            )
//        } else {
//            throw IllegalArgumentException("Invalid email or password") // Бросаем исключение при ошибке
//        }
//    }
//
//    override suspend fun refreshToken() {
//        TODO("Not yet implemented")
//    }
//
//
//    override suspend fun submitLogout(): Boolean {
//        delay(100)
//        authData.isAuthorised = false
//        return true
//    }
//
//    override suspend fun getUser(): User {
//        return User(
//            userName = "${authData.surname} ${authData.name} ${authData.patronymic}",
//            userEmail = authData.email
//        )
//    }
//
//    override suspend fun createUzi(
//        uziUris: List<Uri>, // URI УЗИ файла
//        projection: String, // Проекция УЗИ
//        patientId: String, // ID пациента
//        deviceId: String // ID устройства
//    ): String {
//        // Генерация уникального ID для нового отчета
//        val uziId = "uzi_${System.currentTimeMillis()}"
//
//        // Генерация списка изображений
//        val images = uziUris.mapIndexed { index, uri ->
//            Image(
//                id = "image_${index + 1}",
//                page = index + 1,
//                url = uri
//            )
//        }
//
//        // Генерация данных формирований (заглушка)
//        val formations = List(3) { index ->
//            Formation(
//                ai = index % 2 == 0, // Пример: чередование AI-обнаружения
//                id = "formation_${index + 1}",
//                tirads = Tirads(
//                    tirads_23 = (20..40).random(),
//                    tirads_4 = (10..30).random(),
//                    tirads_5 = (5..15).random()
//                )
//            )
//        }
//
//        // Генерация сегментов (сопоставление с изображениями)
//        val segments = images.flatMap { image ->
//            List(98) { segmentIndex ->
//                Segment(
//                    contor = List(5) { SectorPoint((100..200).random(), (0..100).random()) },
//                    formation_id = formations.random().id,
//                    id = "segment_${image.id}_$segmentIndex",
//                    image_id = image.id,
//                    tirads = Tirads(
//                        tirads_23 = (0..20).random(),
//                        tirads_4 = (0..10).random(),
//                        tirads_5 = (0..5).random()
//                    )
//                )
//            }
//        }
//
//        // Генерация данных Uzi
//        val uzi = Uzi(
//            device_id = (1..1000).random(),
//            id = uziId,
//            patient_id = patientId,
//            projection = "Projection_${(1..3).random()}",
//            url = "wtf",
//            dateOfAdmission = "Время",
//            clinicName = "Клиника"
//        )
//
//        // Создание отчета
//        val reportResponse = ReportResponse(
//            formations = formations,
//            images = images,
//            segments = segments,
//            uzi = uzi
//        )
//
//        // Добавление в хранилище
//        mockUziReportResponses.add(reportResponse)
//        delay(1000)
//
//        // Возвращаем ID созданного Uzi
//        return uziId
//    }
//
//    override suspend fun getUziById(uziId: String): ReportResponse {
//        return mockUziReportResponses.find { it.uzi?.id == uziId }
//            ?: throw NoSuchElementException("Report with Uzi ID $uziId not found")
//    }
    override suspend fun checkAuthorisation(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun submitLogin(email: String, password: String): LoginResponse {
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

    override suspend fun getImageNodesAndSegments(imageId: String): NodesSegmentsResponse {
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
}
