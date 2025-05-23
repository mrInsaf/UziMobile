package com.example.uzi

import android.app.Application
import com.mrinsaf.core.data.local.data_source.TokenStorage
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class UziApp : Application() {
    override fun onCreate() {
        super.onCreate()
        TokenStorage.initialize(this)
    }

}