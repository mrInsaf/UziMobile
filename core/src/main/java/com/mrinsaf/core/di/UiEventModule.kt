package com.mrinsaf.core.di

import com.mrinsaf.core.presentation.ui.event.NewDiagnosticStateChangeEvent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UiEventModule {

    @Provides
    @Singleton
    fun provideNewDiagnosticStateChange(): NewDiagnosticStateChangeEvent = NewDiagnosticStateChangeEvent()
}