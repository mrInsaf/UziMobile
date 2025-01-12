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

    fun onSelectUzi(
        completedDiagnosticId: String,
        downloadedImagesUris: MutableList<Uri>,
        uziImages: List<UziImage>,
        nodesAndSegmentsResponse: NodesSegmentsResponse
    ) {
        viewModelScope.launch {

            _uiState.update { it.copy(
                completedDiagnosticId = completedDiagnosticId,
                downloadedImagesUris = downloadedImagesUris,
                uziImages = uziImages,
                nodesAndSegmentsResponse = nodesAndSegmentsResponse,
            ) }
        }
    }

    fun onUziPageChanged(imageId: String) {
        viewModelScope.launch {
            try {
                val newNodesSegmentsResponse = repository.getImageNodesAndSegments(imageId, true)
                _uiState.update {
                    it.copy(
                        nodesAndSegmentsResponse = newNodesSegmentsResponse
                    )
                }
            } catch (e: Exception) {
                println("Ошибка: ${e.message}")
            }
        }
    }


}