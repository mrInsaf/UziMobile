package com.mrinsaf.core.data.utils.ssh

import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SshTunnelManager() {

    private var session: Session? = null

    suspend fun startTunnel(
        sshHost: String,
        sshPort: Int = 22,
        sshUser: String,
        sshPassword: String? = null,
        privateKey: ByteArray? = null,
        remoteHost: String,
        remotePort: Int,
        localPort: Int
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val jsch = JSch()

            if (privateKey != null) {
                jsch.addIdentity("ssh-key", privateKey, null, null)
            }

            val newSession = jsch.getSession(sshUser, sshHost, sshPort).apply {
                if (sshPassword != null) {
                    setPassword(sshPassword)
                }
                setConfig("StrictHostKeyChecking", "no") // ← Только для dev!
                connect(10_000)
            }

            newSession.setPortForwardingL(localPort, remoteHost, remotePort)
            session = newSession

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun stopTunnel(localPort: Int) {
        session?.let { s ->
            try {
                s.delPortForwardingL("127.0.0.1", localPort)
            } catch (e: Exception) {
                // Игнорируем, если порт уже закрыт
            }
            s.disconnect()
        }
        session = null
    }
}