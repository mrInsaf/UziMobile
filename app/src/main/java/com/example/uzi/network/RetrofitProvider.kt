package com.example.uzi.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitProvider {

    private const val BASE_URL = "http://194.226.121.145:8080/api/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Логировать тело запроса и ответа
    }

    // Настроим OkHttpClient с логированием
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient) // Добавляем OkHttpClient с логированием
            .baseUrl(BASE_URL)
            .build()
    }

    val uziApiService: UziApiService by lazy {
        println("Создаю ретрофит")
        retrofit.create(UziApiService::class.java)
    }
}
