package com.mrinsaf.core.presentation.event.model

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
}