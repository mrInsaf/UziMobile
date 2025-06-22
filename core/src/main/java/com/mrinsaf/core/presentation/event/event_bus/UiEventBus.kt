package com.mrinsaf.core.presentation.event.event_bus

import com.mrinsaf.core.presentation.event.model.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class UiEventBus {
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    suspend fun emitToastEvent(message: String) {
        _uiEvent.emit(UiEvent.ShowToast(message))
    }
}