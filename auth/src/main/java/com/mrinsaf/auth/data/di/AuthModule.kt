package com.mrinsaf.auth.data.di

import android.content.Context
import com.mrinsaf.auth.data.repository.AuthRepositoryImpl
import com.mrinsaf.auth.domain.AuthRepository
import com.mrinsaf.core.data.network.data_source.AuthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
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