package com.example.uzi.ui.viewModel.diagnosticHistory

import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uzi.data.repository.UziServiceRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiagnosticHistoryViewModel(
    val repository: UziServiceRepository,
) : ViewModel() {
    private var _uiState = MutableStateFlow(DiagnosticHistoryUiState())
    val uiState: StateFlow<DiagnosticHistoryUiState>
        get() = _uiState

    fun addUziId(uziId: String) {
        _uiState.update { it.copy(uziIds = (it.uziIds + uziId).toMutableList()) }
    }

    fun onSelectUzi(uziId: String) {
        viewModelScope.launch {
            val uziReportResponse = async { repository.getUziById(uziId) }.await()

            _uiState.update { it.copy(
                currentResponse = uziReportResponse,
                currentUziId = uziId
            ) }
        }
    }

}