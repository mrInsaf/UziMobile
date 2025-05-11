package com.example.uzi

import android.app.Application
import com.mrinsaf.core.data.data_source.local.TokenStorage
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class UziApp : Application() {
    override fun onCreate() {
        super.onCreate()
        TokenStorage.initialize(this)
    }

}