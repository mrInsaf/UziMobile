package com.example.uzi.ui.viewModel.diagnosticHistory

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

class DiagnosticHistoryViewModel(
    val repository: UziServiceRepository,
) : ViewModel() {
    private var _uiState = MutableStateFlow(DiagnosticHistoryUiState())
    val uiState: StateFlow<DiagnosticHistoryUiState>
        get() = _uiState

    fun getPatientUzis(patientId: String) {
        viewModelScope.launch {
            val uziList = repository.getPatientUzis(patientId)
            uziList.forEach { uzi ->
                val id = uzi.id
                val date = uzi.createAt
                val nodesResponse = repository.getUziNodes(id)
                
            }

            _uiState.update {
                it.copy(uziList = uziList)
            }
        }
    }

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
                nodesAndSegmentsResponses = nodesAndSegmentsResponses,
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