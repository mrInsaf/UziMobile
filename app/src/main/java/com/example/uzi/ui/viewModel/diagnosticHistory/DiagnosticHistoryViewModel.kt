package com.example.uzi.ui.viewModel.diagnosticHistory

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uzi.data.models.networkResponses.NodesSegmentsResponse
import com.example.uzi.data.models.networkResponses.UziImage
import com.example.uzi.data.repository.UziServiceRepository
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

    fun fetchUziList(patientId: String) {

    }

    fun onSelectUzi(
        completedDiagnosticId: String,
        downloadedImagesUris: MutableList<Uri>,
        uziImages: List<UziImage>,
        nodesAndSegmentsResponses: List<NodesSegmentsResponse>
    ) {
        viewModelScope.launch {

            _uiState.update { it.copy(
                completedDiagnosticId = completedDiagnosticId,
                downloadedImagesUris = downloadedImagesUris,
                uziImages = uziImages,
                nodesAndSegmentsResponses = nodesAndSegmentsResponses,
            ) }
        }
    }


}