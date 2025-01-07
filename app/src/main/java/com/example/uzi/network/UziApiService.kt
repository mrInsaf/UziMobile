package com.example.uzi.network

import com.example.uzi.data.models.networkRequests.LoginRequest
import com.example.uzi.data.models.networkResponses.LoginResponse
import com.example.uzi.data.models.networkResponses.RefreshResponse
import com.example.uzi.data.models.networkResponses.UziImage
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UziApiService {
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Header("token") refreshToken: String // Передаем refresh token в заголовке
    ): RefreshResponse

    @Multipart
    @POST("uzi/uzis")
    suspend fun createUzi(
        @Header("token") accessToken: String, // Токен доступа
        @Part uziFile: MultipartBody.Part, // Файл УЗИ
        @Part("projection") projection: RequestBody, // Проекция УЗИ
        @Part("patient_id") patientId: RequestBody, // ID пациента
        @Part("device_id") deviceId: RequestBody // ID устройства
    ): Response<String>

    @GET("uzi/uzis/{id}/images")
    suspend fun getUziImages(
        @Header("token") accessToken: String, // Access token в заголовке
        @Path("id") uziId: String // ID УЗИ в пути
    ): List<UziImage>?
}