package com.mrinsaf.profile.di

import com.mrinsaf.core.domain.repository.SubscriptionRepository
import com.mrinsaf.profile.domain.use_case.GetActiveSubscriptionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {
    @Provides
    @Singleton
    fun provideGetActiveSubscriptionUseCase(
        subscriptionRepository: SubscriptionRepository,
    ): GetActiveSubscriptionUseCase =
        GetActiveSubscriptionUseCase(subscriptionRepository)
}