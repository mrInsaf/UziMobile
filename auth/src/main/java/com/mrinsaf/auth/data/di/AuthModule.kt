package com.mrinsaf.auth.data.di

import android.content.Context
import com.mrinsaf.auth.data.repository.AuthRepositoryImpl
import com.mrinsaf.auth.domain.AuthRepository
import com.mrinsaf.core.data.data_source.network.AuthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    private const val BASE_URL = "http://194.226.121.145:8080/api/v1/"
    private val json = Json { ignoreUnknownKeys = true }

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