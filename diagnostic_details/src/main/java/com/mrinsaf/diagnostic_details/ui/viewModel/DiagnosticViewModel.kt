package com.mrinsaf.diagnostic_details.ui.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrinsaf.core.data.models.networkResponses.NodesSegmentsResponse
import com.mrinsaf.core.data.models.basic.UziImage
import com.mrinsaf.core.data.repository.UziServiceRepository
import com.mrinsaf.core.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class DiagnosticViewModel @Inject constructor(
    val repository: UziServiceRepository,
) : ViewModel() {
    private var _uiState = MutableStateFlow(DiagnosticUiState())
    val uiState: StateFlow<DiagnosticUiState>
        get() = _uiState

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()


    fun onDiagnosticCompleted(
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

    fun onSelectUzi(uziId: String, uziDate: String) {
        viewModelScope.launch {
            try {
                val uziImages = repository.getUziImages(uziId)
                val uriResponse= repository.downloadUziFile(uziId)
                val downloadedUziUri = repository.saveUziFileAndGetCacheUri(uziId, uriResponse)
                val uziImageNodesSegments = uziImages.map { image ->
                    async { repository.getImageNodesAndSegments(image.id, true) }
                }.awaitAll()

                _uiState.update {
                    it.copy(
                        currentUziId = uziId,
                        downloadedImagesUris = mutableListOf(downloadedUziUri),
                        uziImages = uziImages,
                        selectedUziNodesAndSegments = uziImageNodesSegments,
                        selectedUziDate = uziDate,
                    )
                }
            } catch (e: Exception) {
                onTokenExpiration()
            }
        }
    }

    fun clearUiState() {
        _uiState.value = DiagnosticUiState()
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


    private fun onTokenExpiration() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.ShowToast("Сессия истекла"))
        }
    }
}