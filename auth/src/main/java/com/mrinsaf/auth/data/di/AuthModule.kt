package com.mrinsaf.auth.data.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mrinsaf.auth.data.repository.AuthInterceptor
import com.mrinsaf.auth.data.repository.AuthRepositoryImpl
import com.mrinsaf.auth.data.repository.TokenAuthenticator
import com.mrinsaf.auth.data.repository.TokenRefresher
import com.mrinsaf.auth.domain.AuthRepository
import com.mrinsaf.core.data.network.AuthApiService
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
object AuthModule {

    private const val BASE_URL = "http://194.226.121.145:8080/api/v1/"
    private val json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(@ApplicationContext context: Context): AuthInterceptor {
        return AuthInterceptor(context)
    }

    @Provides
    @Singleton
    @Named("AuthRetrofit")
    fun provideAuthRetrofit(
        loggingInterceptor: HttpLoggingInterceptor
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(OkHttpClient.Builder().addInterceptor(loggingInterceptor).build())
        .build()

    @Provides
    @Singleton
    fun provideAuthApiService(
        @Named("AuthRetrofit") retrofit: Retrofit
    ): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideTokenRefresher(@ApplicationContext context: Context, apiService: AuthApiService): TokenRefresher {
        return TokenRefresher(
            context = context,
            authApiService = apiService
        )
    }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(tokenRefresher: TokenRefresher): TokenAuthenticator {
        return TokenAuthenticator(tokenRefresher)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApiService: AuthApiService,
        @ApplicationContext context: Context
    ): AuthRepository = AuthRepositoryImpl(
        context,
        authApiService,
    )
}