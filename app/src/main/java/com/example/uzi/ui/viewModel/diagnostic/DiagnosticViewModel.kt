package com.example.uzi.ui.viewModel.diagnostic

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uzi.data.models.networkResponses.NodesSegmentsResponse
import com.example.uzi.data.models.basic.UziImage
import com.example.uzi.data.repository.UziServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiagnosticViewModel(
    val repository: UziServiceRepository,
) : ViewModel() {
    private var _uiState = MutableStateFlow(DiagnosticUiState())
    val uiState: StateFlow<DiagnosticUiState>
        get() = _uiState



    fun onUziCompleted(
        completedDiagnosticId: String,
        downloadedImagesUris: MutableList<Uri>,
        uziImages: List<UziImage>,
        nodesAndSegmentsResponses: List<NodesSegmentsResponse>,
        selectedDiagnosticDate: String,
    ) {
        viewModelScope.launch {

            _uiState.update { it.copy(
                completedDiagnosticId = completedDiagnosticId,
                downloadedImagesUris = downloadedImagesUris,
                uziImages = uziImages,
                completedUziNodesAndSegments = nodesAndSegmentsResponses,
                selectedDiagnosticDate = selectedDiagnosticDate,
            ) }
        }
    }

    fun onSelectUzi(
        uziId: String,
        uziDate: String,
    ) {
        _uiState.update {
            it.copy(
                selectedDiagnosticId = uziId,
                selectedDiagnosticDate = uziDate
            )
        }
    }

    fun openRecommendationBottomSheet() {
        _uiState.update {
            it.copy(isRecommendationSheetVisible = true)
        }
    }

    fun closeRecommendationBottomSheet() {
        _uiState.update {
            it.copy(isRecommendationSheetVisible = false)
        }
    }


}