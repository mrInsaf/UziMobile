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

        runBlocking {
            val result = SshTunnelManager().startTunnel(
                sshHost = SshConfig.sshHost,
                sshPort = SshConfig.sshPort,
                sshUser = SshConfig.sshUser,
                sshPassword = SshConfig.sshPassword,
                privateKey = null,
                remoteHost = SshConfig.remoteHost,
                remotePort = SshConfig.remotePort,
                localPort = SshConfig.localPort,
            )

            result.onSuccess {
                println("yoooooo")
            }.onFailure { e ->
                println("SSH tunnel failed: ${e.message}")
            }
        }

        TokenStorage.initialize(this)
    }

}