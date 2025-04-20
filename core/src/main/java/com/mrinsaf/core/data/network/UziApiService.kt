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
        @Header("Authorization") refreshToken: String // Передаем refresh token в заголовке
    ): RefreshResponse

    @Multipart
    @POST("uzi")
    suspend fun createUzi(
        @Header("Authorization") accessToken: String, // Токен доступа
        @Part uziFile: MultipartBody.Part, // Файл УЗИ
        @Part("projection") projection: RequestBody, // Проекция УЗИ
        @Part("external_id ") externalId: RequestBody, // ID пациента
        @Part("device_id") deviceId: RequestBody // ID устройства
    ): Response<String>

    @GET("uzi/uzis/{id}")
    suspend fun getUzi(
        @Header("Authorization") accessToken: String,
        @Path("id") uziId: String
    ): Uzi

    @GET("uzis/external/{id}")
    suspend fun getUzisByExternalId(
        @Header("Authorization") accessToken: String, // Токен доступа
        @Path("id") externalId: String // ID пациента
    ): UziListResponse

    @GET("uzis/external/{id}")
    suspend fun getUzisByAuthorId(
        @Header("Authorization") accessToken: String,
        @Path("id") authorId: String
    ): UziListResponse

    @GET("uzi/{id}/images")
    suspend fun getUziImages(
        @Header("Authorization") accessToken: String, // Access token в заголовке
        @Path("id") uziId: String // ID УЗИ в пути
    ): List<UziImage>?

    @GET("uzi/image/{id}/nodes-segments")
    suspend fun getImageNodesAndSegments(
        @Header("Authorization") accessToken: String, // Токен в заголовке
        @Path("id") imageId: String // ID изображения в URL
    ): NodesSegmentsResponse

    @GET("uzi/{id}/nodes")
    suspend fun getUziNodes(
        @Path("id") uziId: String,
        @Header("Authorization") accessToken: String
    ): UziNodesResponse

    @GET("download/{uzi_id}/{image_id}")
    suspend fun downloadUziImage(
        @Header("Authorization") token: String,
        @Path("uzi_id") uziId: String,
        @Path("image_id") imageId: String
    ): Response<ResponseBody>
}
