package com.mrinsaf.auth.data.repository

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AuthEventBus {
    private val _authRequired = MutableSharedFlow<Unit>()
    val authRequired = _authRequired.asSharedFlow()

    suspend fun emitAuthRequired() {
        _authRequired.emit(Unit)
    }
}