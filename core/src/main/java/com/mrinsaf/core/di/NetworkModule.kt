package com.mrinsaf.core.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mrinsaf.core.data.network.data_source.AuthApiService
import com.mrinsaf.core.data.network.data_source.UziApiService
import com.mrinsaf.core.data.utils.auth_utils.AuthInterceptor
import com.mrinsaf.core.data.utils.auth_utils.TokenAuthenticator
import com.mrinsaf.core.data.utils.auth_utils.TokenRefresher
import com.mrinsaf.core.data.network.repository.NetworkUziServiceRepository
import com.mrinsaf.core.domain.repository.UziServiceRepository
import com.mrinsaf.core.presentation.ui.event.NewDiagnosticStateChangeEvent
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
object NetworkModule {

    private const val BASE_URL = "http://194.226.121.145:8080/api/v1/"
    private val json = Json { ignoreUnknownKeys = true }

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
    @Named("ApiServiceRetrofit")
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideUziApiService(
        @Named("ApiServiceRetrofit")
        retrofit: Retrofit
    ): UziApiService = retrofit.create(UziApiService::class.java)

    @Provides
    @Singleton
    fun provideUziServiceRepository(
        uziApiService: UziApiService,
        newDiagnosticStateChangeEvent: NewDiagnosticStateChangeEvent,
        @ApplicationContext context: Context
    ): UziServiceRepository =
        NetworkUziServiceRepository(
            uziApiService = uziApiService,
            newDiagnosticStateChangeEvent = newDiagnosticStateChangeEvent,
            context = context
    )
}
