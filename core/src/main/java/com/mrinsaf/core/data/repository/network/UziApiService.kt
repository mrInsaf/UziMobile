package com.mrinsaf.core.data.repository.network

import com.mrinsaf.core.data.models.basic.Node
import com.mrinsaf.core.data.models.basic.Uzi
import com.mrinsaf.core.data.models.basic.UziDevice
import com.mrinsaf.core.data.models.basic.UziImage
import com.mrinsaf.core.data.models.networkResponses.NodesSegmentsResponse
import com.mrinsaf.core.data.models.networkResponses.PostUziResponse
import com.mrinsaf.core.data.models.networkResponses.UziListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UziApiService {
    @Multipart
    @POST("uzi")
    suspend fun createUzi(
        @Part uziFile: MultipartBody.Part, // Файл УЗИ
        @Part("projection") projection: RequestBody, // Проекция УЗИ
        @Part("external_id") externalId: RequestBody, // ID пациента
        @Part("device_id") deviceId: RequestBody // ID устройства
    ): Response<PostUziResponse>

    @GET("uzi/{id}")
    suspend fun getUzi(
        @Path("id") uziId: String
    ): Uzi

    @GET("uzis/external/{id}")
    suspend fun getUzisByExternalId(
        @Path("id") externalId: String // ID пациента
    ): List<Uzi>

    @GET("uzis/external/{id}")
    suspend fun getUzisByAuthorId(
        @Path("id") authorId: String
    ): UziListResponse

    @GET("uzi/{id}/images")
    suspend fun getUziImages(
        @Path("id") uziId: String // ID УЗИ в пути
    ): List<UziImage>?

    @GET("uzi/image/{id}/nodes-segments")
    suspend fun getImageNodesAndSegments(
        @Path("id") imageId: String // ID изображения в URL
    ): NodesSegmentsResponse

    @GET("uzi/{id}/nodes")
    suspend fun getUziNodes(
        @Path("id") uziId: String,
    ): List<Node>

    @GET("download/{uzi_id}/{image_id}")
    suspend fun downloadUziImage(
        @Path("uzi_id") uziId: String,
        @Path("image_id") imageId: String
    ): Response<ResponseBody>

    @GET("uzi/devices")
    suspend fun getUziDevices(): Response<List<UziDevice>>
}
