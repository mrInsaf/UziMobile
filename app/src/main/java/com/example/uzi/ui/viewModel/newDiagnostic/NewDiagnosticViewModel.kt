package com.example.uzi.ui.viewModel.newDiagnostic

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uzi.data.models.networkResponses.NodesSegmentsResponse
import com.example.uzi.data.repository.UziServiceRepository
import com.example.uzi.ui.UiEvent
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class NewDiagnosticViewModel(
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
            val imageUris = uiState.value.selectedImageUris
            val dateOfAdmission = uiState.value.dateOfAdmission
            val clinicName = uiState.value.clinicName

            // Обновляем UI-состояние перед началом диагностики
            _uiState.update { it.copy(
                isDiagnosticSent = true,
                completedDiagnosticId = "",
            ) }

            try {
                // Создание УЗИ
//                val diagnosticId = repository.createUzi(
//                    uziUris = imageUris,
//                    projection = "long",
//                    patientId = "72881f74-1d10-4d93-9002-5207a83729ed", // TODO: заменить на ID авторизованного пользователя
//                    deviceId = "1",
//                )

                val diagnosticId = "f09282f0-eb96-4f71-a7ee-332b532c9dc8"
                println("diagnosticId: $diagnosticId")

                val uziImages = repository.getUziImages(diagnosticId)

                // Загрузка всех изображений УЗИ одним запросом
                val downloadedUziResponseBody = repository.downloadUziFile(
                    uziId = diagnosticId
                )
                println("УЗИ успешно загружено")

                // Сохранение УЗИ в локальное хранилище
                val downloadedUziUri = repository.saveUziFileAndGetCacheUri(diagnosticId, downloadedUziResponseBody)
                println("Сохраненный URI УЗИ: $downloadedUziUri")

                val firstImageNodesSegments = async {
                    repository.getImageNodesAndSegments(uziImages.first().id, false)
                }.await()

                println("Успешно получил сегменты первого снимка: $firstImageNodesSegments")

                val remainingImageNodesSegments = uziImages.drop(1).map { image ->
                    async {
                        println("Запрашиваю ноды для ${image.id}")
                        try {
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
                }.awaitAll()

                val uziImageNodesSegments = mutableListOf(firstImageNodesSegments)
                uziImageNodesSegments.addAll(remainingImageNodesSegments.filterNotNull()) // Фильтруем null-значения


                // Обновляем состояние с результатами диагностики
                _uiState.update { state ->
                    state.copy(
                        completedDiagnosticId = diagnosticId,
                        downloadedImagesUris = imageUris.toMutableList(), // Устанавливаем URI загруженного УЗИ
                        uziImages = uziImages,
                        nodesAndSegmentsResponses = uziImageNodesSegments
                    )
                }

                println("uziImageNodesSegments: $uziImageNodesSegments")
            } catch (e: HttpException) {
                // Обработка ошибок авторизации
                onTokenExpiration()
                _uiState.update {
                    it.copy(
                        currentScreenIndex = 0,
                        selectedImageUris = emptyList()
                    )
                }
                println("Ошибка HTTP: $e")
            } catch (e: Exception) {
                // Общая обработка ошибок
                println("Ошибка: $e")
                throw e
            }
        }
    }

    private fun onTokenExpiration() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.ShowToast("Сессия истекла"))
        }
    }
}