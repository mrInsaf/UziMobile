package com.mrinsaf.core.data.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mrinsaf.core.data.network.UziApiService
import com.mrinsaf.core.data.repository.auth.AuthInterceptor
import com.mrinsaf.core.data.repository.auth.TokenAuthenticator
import com.mrinsaf.core.data.repository.network.NetworkUziServiceRepository
import com.mrinsaf.core.domain.repository.UziServiceRepository
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://194.226.121.145:8080/api/v1/"
    private val json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .addNetworkInterceptor { chain ->
                val response = chain.proceed(chain.request())
                val bodyStr = response.peekBody(Long.MAX_VALUE).string()
                return@addNetworkInterceptor  if (response.code == 500 && bodyStr.contains("parse token", ignoreCase = true)) {
                    response
                        .newBuilder()
                        .code(401)
                        .header("WWW-Authenticate", "Bearer")
                        .message("Unauthorized")
                        .build()
                } else {
                    response
                }
            }
            .authenticator(tokenAuthenticator)
            .cache(null)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
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
        NetworkUziServiceRepository(
            uziApiService,
            context
        )
}
