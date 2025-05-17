package com.mrinsaf.core.presentation.ui.event

import com.mrinsaf.core.presentation.ui.ui_state.DiagnosticProgressStateDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewDiagnosticStateChangeEvent {
    private val _diagnosticProgressState = MutableStateFlow<DiagnosticProgressStateDetail>(
        DiagnosticProgressStateDetail.UploadingUzi
    )
    val diagnosticProgressState = _diagnosticProgressState.asStateFlow()

    fun updateDiagnosticProgressState(progressState: DiagnosticProgressStateDetail) {
        _diagnosticProgressState.value = progressState
    }
    
}