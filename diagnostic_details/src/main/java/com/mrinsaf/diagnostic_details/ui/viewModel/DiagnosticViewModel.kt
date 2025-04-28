package com.mrinsaf.diagnostic_details.ui.viewModel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrinsaf.core.data.models.networkResponses.NodesSegmentsResponse
import com.mrinsaf.core.data.models.basic.UziImage
import com.mrinsaf.core.data.repository.UziServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DiagnosticViewModel @Inject constructor(
    val repository: UziServiceRepository,
) : ViewModel() {
    private var _uiState = MutableStateFlow(DiagnosticUiState())
    val uiState: StateFlow<DiagnosticUiState>
        get() = _uiState

    fun onDiagnosticCompleted(
        uziId: String,
        imagesUris: Uri,
        uziImages: List<UziImage>,
        uziImagesBmp: Map<String, Bitmap>,
        nodesAndSegmentsResponses: List<NodesSegmentsResponse>,
        selectedUziDate: String,
    ) {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    currentUziId = uziId,
                    downloadedImageUri = imagesUris,
                    uziImages = uziImages,
                    uziImagesBmp = uziImagesBmp,
                    selectedUziNodesAndSegments = nodesAndSegmentsResponses,
                    selectedUziDate = selectedUziDate,
                    numberOfImages = uziImages.size,
                )
            }
        }
    }

    suspend fun onSelectUzi(uziId: String, uziDate: String) {
        try {
            println("onSelectUzi стартовал для uziId: $uziId, uziDate: $uziDate")

            println("Вызов repository.getUziImages")
            val uziImages = repository.getUziImages(uziId)
            println("uziImages получен: ${uziImages.size} элементов")

            val numberOfImages = uziImages.size

            viewModelScope.launch {
                uziImages.forEachIndexed { i, image ->
                    println("скачиваю картинку $i")
                    val imageId = image.id

                    // Пропускаем скачивание, если картинка уже есть в Map
                    if (_uiState.value.uziImagesBmp.containsKey(imageId)) {
                        println("Картинка с id $imageId уже скачана, пропускаем")
                        return@forEachIndexed
                    }

                    val responseBody = repository.downloadUziImage(uziId, imageId)
                    val bitmap = responseBody.byteStream().use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)
                    }

                    _uiState.update { currentState ->
                        currentState.copy(
                            uziImagesBmp = currentState.uziImagesBmp + (imageId to bitmap)
                        )
                    }
                }
            }

            println("Переход в Dispatchers.Default для обработки uziImageNodesSegments")
            viewModelScope.launch(Dispatchers.Default) {
                val uziImageNodesSegments = uziImages.mapIndexed { index, image ->
                    async {
                        println("Запрос getImageNodesAndSegments для image.id: ${image.id} (index $index)")
                        try {
                            repository.getImageNodesAndSegments(image.id, true)
                        } catch (e: Exception) {
                            println(e)
                            null
                        }
                    }
                }.awaitAll()
                println("uziImageNodesSegments получен: ${uziImageNodesSegments.size} элементов")
                _uiState.update {
                    println("Обновление selectedUziNodesAndSegments в _uiState")
                    it.copy(selectedUziNodesAndSegments = uziImageNodesSegments.filterNotNull())
                }
            }
            println("Обновление оставшихся полей _uiState...")
            _uiState.update {
                println("currentUziId: $uziId")
                println("uziImages: ${uziImages.size} элементов")
                println("selectedUziDate: $uziDate")

                it.copy(
                    currentUziId = uziId,
                    uziImages = uziImages,
                    selectedUziDate = uziDate,
                    numberOfImages = numberOfImages,
                )
            }

            println("все збс")
        } catch (e: Exception) {
            println("Ошибка в onSelectUzi: ${e.message}")
            e.printStackTrace()
        }
    }

    fun clearUiState() {
        println("clearing uistate")
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
}