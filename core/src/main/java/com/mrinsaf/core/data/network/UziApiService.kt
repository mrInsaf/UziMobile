package com.mrinsaf.core.data.network

import com.mrinsaf.core.data.models.networkRequests.LoginRequest
import com.mrinsaf.core.data.models.networkResponses.NodesSegmentsResponse
import com.mrinsaf.core.data.models.networkResponses.RefreshResponse
import com.mrinsaf.core.data.models.basic.Uzi
import com.mrinsaf.core.data.models.basic.UziImage
import com.mrinsaf.core.data.models.networkResponses.UziListResponse
import com.mrinsaf.core.data.models.networkResponses.UziNodesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import com.mrinsaf.core.data.models.networkRequests.RegPatientRequest
import com.mrinsaf.core.data.models.networkResponses.LoginResponse
import retrofit2.http.POST
import retrofit2.http.Part
import com.mrinsaf.core.data.models.networkResponses.RegPatientResponse
import retrofit2.http.Path

interface UziApiService {
    @POST("reg/patient")
    suspend fun regPatient(
        @Body request: RegPatientRequest
    ): Response<RegPatientResponse>

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("refresh")
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

    @GET("uzi/images/{id}/nodes-segments")
    suspend fun getImageNodesAndSegments(
        @Header("token") accessToken: String, // Токен в заголовке
        @Path("id") imageId: String // ID изображения в URL
    ): NodesSegmentsResponse

    @GET("download/uzi/{uzi_id}/image/{image_id}")
    suspend fun downloadUziImage(
        @Header("token") token: String,
        @Path("uzi_id") uziId: String,
        @Path("image_id") imageId: String
    ): Response<ResponseBody> // ResponseBody для бинарных данных

    @GET("download/uzi/{id}")
    suspend fun downloadUzi(
        @Header("token") token: String,
        @Path("id") uziId: String
    ): Response<ResponseBody> // ResponseBody для получения бинарных данных

    @GET("uzi/patient/{id}/uzis")
    suspend fun getPatientUzis(
        @Header("token") accessToken: String, // Токен доступа
        @Path("id") patientId: String // ID пациента
    ): UziListResponse

    @GET("uzi/uzis/{id}/nodes")
    suspend fun getUziNodes(
        @Path("id") uziId: String,
        @Header("token") accessToken: String
    ): UziNodesResponse

    @GET("uzi/uzis/{id}")
    suspend fun getUzi(
        @Header("token") accessToken: String,
        @Path("id") uziId: String
    ): Uzi
}
