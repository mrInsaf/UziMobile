package com.mrinsaf.subscription.di

import com.mrinsaf.core.domain.repository.SubscriptionRepository
import com.mrinsaf.subscription.data.data_source.SubscriptionApi
import com.mrinsaf.subscription.data.repository.SubscriptionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SubscriptionModule {

    @Provides
    @Singleton
    fun provideSubscriptionApiService(
        @Named("ApiServiceRetrofit") retrofit: Retrofit
    ): SubscriptionApi =
        retrofit.create(SubscriptionApi::class.java)

    @Provides
    @Singleton
    fun provideSubscriptionRepository(
        apiService: SubscriptionApi
    ): SubscriptionRepository = SubscriptionRepositoryImpl(
        subscriptionApi = apiService
    )
}