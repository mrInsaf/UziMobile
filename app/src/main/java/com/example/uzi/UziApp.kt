package com.example.uzi

import android.app.Application
import androidx.lifecycle.lifecycleScope
import com.mrinsaf.core.data.config.SshConfig
import com.mrinsaf.core.data.local.data_source.TokenStorage
import com.mrinsaf.core.data.utils.ssh.SshTunnelManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltAndroidApp
class UziApp : Application() {
    override fun onCreate() {
        super.onCreate()
        TokenStorage.initialize(this)
    }
}