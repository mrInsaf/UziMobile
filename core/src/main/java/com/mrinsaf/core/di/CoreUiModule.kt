package com.mrinsaf.core.di

import android.content.Context
import com.mrinsaf.core.presentation.event.NewDiagnosticStateChangeEvent
import com.mrinsaf.core.presentation.event.event_bus.UiEventBus
import com.mrinsaf.core.presentation.payment_navigator.PaymentNavigator
import com.mrinsaf.core.presentation.payment_navigator.PaymentNavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreUiModule {
    @Provides
    @Singleton
    fun provideNewDiagnosticStateChange(): NewDiagnosticStateChangeEvent = NewDiagnosticStateChangeEvent()

    @Provides
    @Singleton
    fun provideUiEventBus(): UiEventBus = UiEventBus()

    @Provides
    @Singleton
    fun providePaymentNavigator(
        @ApplicationContext context: Context
    ): PaymentNavigator = PaymentNavigatorImpl(context)
}