package com.example.uzi.data.repository

import android.net.Uri
import com.example.uzi.data.mock.MockAuthData
import com.example.uzi.data.mock.MockUziReport
import com.example.uzi.data.models.Formation
import com.example.uzi.data.models.Image
import com.example.uzi.data.models.ReportResponse
import com.example.uzi.data.models.SectorPoint
import com.example.uzi.data.models.Segment
import com.example.uzi.data.models.Tirads
import com.example.uzi.data.models.User
import com.example.uzi.data.models.Uzi
import kotlinx.coroutines.delay
import java.text.Normalizer.Form

class MockUziServiceRepository : UziServiceRepository {
    private val authData = MockAuthData()
    private val mockUziReports: MutableList<MockUziReport> = mutableListOf()
    private val mockUziReportResponses: MutableList<ReportResponse> = mutableListOf()

    override suspend fun checkAuthorisation(): Boolean {
        delay(100) // Имитация задержки
        return authData.isAuthorised
    }

    override suspend fun submitLogin(
        email: String,
        password: String
    ): Boolean {
        delay(100)
        return email == authData.email && password == authData.password
    }


    override suspend fun submitLogout(): Boolean {
        delay(100)
        authData.isAuthorised = false
        return true
    }

    override suspend fun getUser(): User {
        return User(
            userName = "${authData.surname} ${authData.name} ${authData.patronymic}",
            userEmail = authData.email
        )
    }

    override suspend fun createUzi(
        patientId: String,
        imageUris: List<Uri>,
        dateOfAdmission: String,
        clinicName: String
    ): String {
        // Генерация уникального ID для нового отчета
        val uziId = "uzi_${System.currentTimeMillis()}"

        // Генерация списка изображений
        val images = imageUris.mapIndexed { index, uri ->
            Image(
                id = "image_${index + 1}",
                page = index + 1,
                url = uri
            )
        }

        // Генерация данных формирований (заглушка)
        val formations = List(3) { index ->
            Formation(
                ai = index % 2 == 0, // Пример: чередование AI-обнаружения
                id = "formation_${index + 1}",
                tirads = Tirads(
                    tirads_23 = (20..40).random(),
                    tirads_4 = (10..30).random(),
                    tirads_5 = (5..15).random()
                )
            )
        }

        // Генерация сегментов (сопоставление с изображениями)
        val segments = images.flatMap { image ->
            List(2) { segmentIndex ->
                Segment(
                    contor = List(5) { SectorPoint((100..200).random(), (0..100).random()) },
                    formation_id = formations.random().id,
                    id = "segment_${image.id}_$segmentIndex",
                    image_id = image.id,
                    tirads = Tirads(
                        tirads_23 = (10..20).random(),
                        tirads_4 = (5..10).random(),
                        tirads_5 = (2..5).random()
                    )
                )
            }
        }

        // Генерация данных Uzi
        val uzi = Uzi(
            device_id = (1..1000).random(),
            id = uziId,
            patient_id = patientId,
            projection = "Projection_${(1..3).random()}",
            url = "wtf",
            dateOfAdmission = dateOfAdmission,
            clinicName = clinicName
        )

        // Создание отчета
        val reportResponse = ReportResponse(
            formations = formations,
            images = images,
            segments = segments,
            uzi = uzi
        )

        // Добавление в хранилище
        mockUziReportResponses.add(reportResponse)
        delay(4000)

        // Возвращаем ID созданного Uzi
        return uziId
    }

    override suspend fun getUziById(uziId: String): ReportResponse {
        return mockUziReportResponses.find { it.uzi?.id == uziId }
            ?: throw NoSuchElementException("Report with Uzi ID $uziId not found")
    }
}
