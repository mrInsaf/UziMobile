package com.mrinsaf.newdiagnostic.ui.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrinsaf.core.data.models.networkResponses.NodesSegmentsResponse
import com.mrinsaf.core.data.models.basic.Uzi
import com.mrinsaf.core.data.models.basic.UziImage
import com.mrinsaf.core.ui.UiEvent
import com.mrinsaf.core.data.repository.UziServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class NewDiagnosticViewModel @Inject constructor(
    val repository: UziServiceRepository,
) : ViewModel() {
    private var _uiState = MutableStateFlow(NewDiagnosticUiState())
    val uiState: StateFlow<NewDiagnosticUiState>
        get() = _uiState

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onDatePick(newDate: String) {
        println("yooooo")
        _uiState.update { it.copy(dateOfAdmission = newDate) }
        println(uiState.value.dateOfAdmission)
    }

    fun onNextScreenButtonClick() {
        val currentScreenIndex = uiState.value.currentScreenIndex
        _uiState.update { it.copy(currentScreenIndex = currentScreenIndex + 1) }
        println(uiState.value.currentScreenIndex)
    }

    fun onPreviousButtonClick() {
        val currentScreenIndex = uiState.value.currentScreenIndex
        _uiState.update { it.copy(currentScreenIndex = currentScreenIndex - 1) }
    }

    fun onClinicNameInput(newClinicName: String) {
        _uiState.update { it.copy(clinicName = newClinicName) }
    }

    fun onSaveResultsCheck() {
        val saveResultsChecked = uiState.value.saveResultsChecked
        _uiState.update { it.copy(saveResultsChecked = !saveResultsChecked) }
    }

    fun onPhotoPickResult(uris: List<Uri>) {
        _uiState.update { it.copy(selectedImageUris = uris) }
        println("uiState.value.selectedImageUris: ${uiState.value.selectedImageUris}")
    }

    fun onDiagnosticStart(userId: String = "1") {
        viewModelScope.launch {
            try {
                updateUiBeforeDiagnosticStart()

//                val diagnosticId = createDiagnostic()
                val diagnosticId = "f00941dd-3769-497a-a813-cc457f6053f9"
                val uziInformation = repository.getUzi(diagnosticId)
                val uziImages = fetchUziImages(diagnosticId)
                val downloadedUziUri = downloadAndSaveUzi(diagnosticId)

                val uziImageNodesSegments = fetchImageNodesSegments(uziImages)

                updateUiAfterDiagnosticCompletion(
                    diagnosticId, downloadedUziUri, uziImages, uziImageNodesSegments,
                    uziInformation = uziInformation
                )

            } catch (e: HttpException) {
                handleHttpException(e)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        diagnosticProcessState = DiagnosticProcessState.Failure
                    )
                }
                handleGeneralException(e)
            }
        }
    }

    private fun updateUiBeforeDiagnosticStart() {
        _uiState.update { it.copy(
            diagnosticProcessState = DiagnosticProcessState.Sending,
        ) }
    }

    private suspend fun createDiagnostic(): String {
        _uiState.update {
            it.copy(
                diagnosticProcessState = DiagnosticProcessState.Sending,
            )
        }
        val uziId = repository.createUzi(
            uziUris = uiState.value.selectedImageUris,
            projection = "long",
            patientId = "72881f74-1d10-4d93-9002-5207a83729ed", // TODO: заменить на ID авторизованного пользователя
            deviceId = "1",
        ).also {
            println("diagnosticId: $it")
        }
        return uziId
    }

    private suspend fun fetchUziImages(diagnosticId: String): List<UziImage> {
        return repository.getUziImages(diagnosticId)
    }

    private suspend fun downloadAndSaveUzi(diagnosticId: String): Uri {
        val responseBody = repository.downloadUziFile(diagnosticId)
        println("УЗИ успешно загружено")
        return repository.saveUziFileAndGetCacheUri(diagnosticId, responseBody).also {
            println("Сохраненный URI УЗИ: $it")
        }
    }

    private suspend fun fetchImageNodesSegments(uziImages: List<UziImage>): List<NodesSegmentsResponse> =
        coroutineScope {
            val firstImageNodesSegments = async {
                repository.getImageNodesAndSegments(uziImages.first().id, false)
            }.await()

            println("Успешно получил сегменты первого снимка: $firstImageNodesSegments")

            val remainingImageNodesSegments = uziImages.drop(1).map { image ->
                async { fetchImageNodesForSingleImage(image) }
            }.awaitAll()

            listOf(firstImageNodesSegments) + remainingImageNodesSegments.filterNotNull()
        }


    private suspend fun fetchImageNodesForSingleImage(image: UziImage): NodesSegmentsResponse? {
        return try {
            println("Запрашиваю ноды для ${image.id}")
            repository.getImageNodesAndSegments(image.id, true)
        } catch (e: Exception) {
            println("Ошибка при получении нод для изображения с ID ${image.id}: ${e.message}")
            if (e.message?.contains("Не удалось выполнить запрос после") == true) {
                println("Пустой ответ для изображения с ID ${image.id}")
                null
            } else {
                println("Серьезная ошибка $e")
                throw e
            }
        }
    }

    private fun updateUiAfterDiagnosticCompletion(
        diagnosticId: String,
        downloadedUziUri: Uri,
        uziImages: List<UziImage>,
        uziImageNodesSegments: List<NodesSegmentsResponse>,
        uziInformation: Uzi?,
    ) {
        _uiState.update { state ->
            state.copy(
                diagnosticProcessState = DiagnosticProcessState.Success(diagnosticId),
                completedDiagnosticId = diagnosticId,
                downloadedImagesUris = uiState.value.selectedImageUris.toMutableList(),
                uziImages = uziImages,
                nodesAndSegmentsResponses = uziImageNodesSegments,
                completedDiagnosticInformation = uziInformation,
            )
        }
        println("uziImageNodesSegments: $uziImageNodesSegments")
    }

    private fun handleHttpException(e: HttpException) {
        onTokenExpiration()
        _uiState.update {
            it.copy(
                currentScreenIndex = 0,
                selectedImageUris = emptyList()
            )
        }
        println("Ошибка HTTP: $e")
    }

    private fun handleGeneralException(e: Exception) {
        println("Ошибка: $e")
    }

    private fun onTokenExpiration() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.ShowToast("Сессия истекла"))
        }
    }
}