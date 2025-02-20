package com.example.uzi.ui.viewModel.diagnostic

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrinsaf.core.data.models.networkResponses.NodesSegmentsResponse
import com.mrinsaf.core.data.models.basic.UziImage
import com.mrinsaf.core.data.repository.UziServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiagnosticViewModel @Inject constructor(
    val repository: UziServiceRepository,
) : ViewModel() {
    private var _uiState = MutableStateFlow(DiagnosticUiState())
    val uiState: StateFlow<DiagnosticUiState>
        get() = _uiState



    fun onUziSelected(
        uziId: String,
        imagesUris: MutableList<Uri>,
        uziImages: List<UziImage>,
        nodesAndSegmentsResponses: List<NodesSegmentsResponse>,
        selectedUziDate: String,
    ) {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    currentUziId = uziId,
                    downloadedImagesUris = imagesUris,
                    uziImages = uziImages,
                    selectedUziNodesAndSegments = nodesAndSegmentsResponses,
                    selectedUziDate = selectedUziDate,
                )
            }
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