package com.mrinsaf.core.data.repository.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mrinsaf.core.data.network.UziApiService
import com.mrinsaf.core.data.repository.MockUziServiceRepository
import com.mrinsaf.core.data.repository.NetworkUziServiceRepository
import com.mrinsaf.core.data.repository.UziServiceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    private const val BASE_URL = "http://194.226.121.145:8080/api/v1/"
    private val json = Json { ignoreUnknownKeys = true }
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .cache(null)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideUziApiService(retrofit: Retrofit): UziApiService = retrofit.create(UziApiService::class.java)

    @Provides
    @Singleton
    fun provideUziServiceRepository(
        uziApiService: UziApiService,
        @ApplicationContext context: Context
    ): UziServiceRepository =
        NetworkUziServiceRepository(uziApiService, context)
//        MockUziServiceRepository()
}
