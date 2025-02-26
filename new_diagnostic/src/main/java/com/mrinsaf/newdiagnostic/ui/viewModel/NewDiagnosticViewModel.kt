package com.mrinsaf.newdiagnostic.ui.viewModel

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrinsaf.core.data.models.networkResponses.NodesSegmentsResponse
import com.mrinsaf.core.data.models.basic.Uzi
import com.mrinsaf.core.data.models.basic.UziImage
import com.mrinsaf.core.data.repository.UziServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class NewDiagnosticViewModel @Inject constructor(
    val repository: UziServiceRepository,
    @ApplicationContext val context: Context
) : ViewModel() {
    private var _uiState = MutableStateFlow(NewDiagnosticUiState())
    val uiState: StateFlow<NewDiagnosticUiState>
        get() = _uiState

    private var uploadDiagnosticJob: Job? = null

    fun onDatePick(newDate: String) {
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

    fun onPhotoPickResult(uri: Uri) {
        val fileName = getFileName(uri) ?: "Без названия"
        _uiState.update {
            it.copy(
                selectedImageUri = uri,
                selectedImageName = fileName
            )
        }
    }

    fun onDiagnosticStart(userId: String = "1") {
        uploadDiagnosticJob = viewModelScope.launch {
            try {
                updateUiBeforeDiagnosticStart()

                val diagnosticId = createDiagnostic().also {
                    _uiState.update { it.copy(isUziPosted = true) }
                }
//            val diagnosticId = "f00941dd-3769-497a-a813-cc457f6053f9"
                val uziInformation = repository.getUzi(diagnosticId)
                val uziImages = fetchUziImages(diagnosticId)

                uziImages.forEachIndexed { i, image ->
                    println("скачиваю картинку $i")
                    val imageId = image.id

                    // Пропускаем скачивание, если картинка уже есть в Map
                    if (_uiState.value.uziImagesBmp.containsKey(imageId)) {
                        println("Картинка с id $imageId уже скачана, пропускаем")
                        return@forEachIndexed
                    }

                    val responseBody = repository.downloadUziImage(diagnosticId, imageId)
                    val bitmap = responseBody.byteStream().use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)
                    }

                    _uiState.update { currentState ->
                        currentState.copy(
                            uziImagesBmp = currentState.uziImagesBmp + (imageId to bitmap)
                        )
                    }
                }

                val uziImageNodesSegments = fetchImageNodesSegments(uziImages)

                updateUiAfterDiagnosticCompletion(
                    diagnosticId, uziImages, uziImageNodesSegments,
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

    fun returnToUploadScreen() {
        try {
            uploadDiagnosticJob?.cancel()
                .also { println("Ожидание результатов узи отменено") }
        }
        catch (e: Exception) {
            println(e)
        }
        _uiState.update {
            NewDiagnosticUiState()
        }
    }

    private fun updateUiBeforeDiagnosticStart() {
        _uiState.update {
            it.copy(
                diagnosticProcessState = DiagnosticProcessState.Sending,
            )
        }
    }

    private suspend fun createDiagnostic(): String {
        _uiState.update {
            it.copy(
                diagnosticProcessState = DiagnosticProcessState.Sending,
            )
        }

        val uziId = repository.createUzi(
            uziUris = uiState.value.selectedImageUri!!,
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

    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex != -1) {
                fileName = cursor.getString(nameIndex)
            }
        }
        return fileName
    }

    private fun updateUiAfterDiagnosticCompletion(
        diagnosticId: String,
        uziImages: List<UziImage>,
        uziImageNodesSegments: List<NodesSegmentsResponse>,
        uziInformation: Uzi?,
    ) {
        _uiState.update { state ->
            state.copy(
                diagnosticProcessState = DiagnosticProcessState.Success(diagnosticId),
                completedDiagnosticId = diagnosticId,
                downloadedImagesUri = uiState.value.selectedImageUri,
                uziImages = uziImages,
                nodesAndSegmentsResponses = uziImageNodesSegments,
                completedDiagnosticInformation = uziInformation,
            )
        }
        println("uziImageNodesSegments: $uziImageNodesSegments")
    }

    private fun handleHttpException(e: HttpException) {
        _uiState.update {
            it.copy(
                currentScreenIndex = 0,
                selectedImageUri = null
            )
        }
        println("Ошибка HTTP: $e")
    }

    private fun handleGeneralException(e: Exception) {
        println("Ошибка: $e")
    }

}