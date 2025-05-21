package com.mrinsaf.newdiagnostic.ui.viewModel

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrinsaf.core.data.network.dto.network_responses.NodesSegmentsResponse
import com.mrinsaf.core.domain.model.basic.Uzi
import com.mrinsaf.core.domain.model.basic.UziImage
import com.mrinsaf.core.domain.repository.UziServiceRepository
import com.mrinsaf.core.presentation.ui.event.NewDiagnosticStateChangeEvent
import com.mrinsaf.core.presentation.ui.ui_state.DiagnosticProgressStateDetail
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
    val newDiagnosticStateChangeEvent: NewDiagnosticStateChangeEvent,
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
        println(fileName)
        _uiState.update {
            it.copy(
                selectedImageUri = uri,
                selectedImageName = fileName
            )
        }
    }

    fun onDiagnosticStart(patientId: String) {
        uploadDiagnosticJob = viewModelScope.launch {
            try {
                println("=== Начало диагностики ===")

                updateUiBeforeDiagnosticStart()
                println("UI обновлен перед стартом диагностики")

                println("Создание диагностической записи...")
                val diagnosticId = createDiagnostic(patientId).also {
                    println("Diagnostic ID получен: $it")
                }
                _uiState.update { state ->
                    state.copy(isUziPosted = true)
                }
                updateUploadUziProgressState(DiagnosticProgressStateDetail.DiagnosticWaiting.New)
                println("Используется Diagnostic ID: $diagnosticId")

                println("Получение информации о УЗИ...")
                launch {
                    newDiagnosticStateChangeEvent.diagnosticProgressState.collect { progressStateDetail ->
                        updateUploadUziProgressState(progressStateDetail)
                    }
                }

                val uziInformation = repository.getUzi(diagnosticId)

                if (uziInformation == null) {
                    println("Информация о УЗИ не получена. Отмена диагностики.")
                    _uiState.update {
                        it.copy(
                            diagnosticProcessState = DiagnosticProcessState.Failure,
                        )
                    }
                    return@launch
                }

                println("Информация о УЗИ получена: $uziInformation")

                println("Получение списка изображений УЗИ...")
                val uziImages = fetchUziImages(diagnosticId)
                println("Найдено изображений: ${uziImages.size}")

                uziImages.forEachIndexed { i, image ->
                    println("Обработка изображения $i (ID: ${image.id})")

                    if (_uiState.value.uziImagesBmp.containsKey(image.id)) {
                        println("Картинка с ID ${image.id} уже загружена, пропуск")
                        return@forEachIndexed
                    }

                    println("Скачивание изображения ${image.id}...")
                    val responseBody = repository.downloadUziImage(diagnosticId, image.id)
                    println("Ответ от сервера: ${responseBody.contentType()} (${responseBody.contentLength()} байт)")

                    val bitmap = responseBody.byteStream().use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)
                    }
                    println("Битмап создан: ${bitmap.width}x${bitmap.height} пикселей")

                    updateImageDownloadState(
                        currentImage = i,
                        totalImages = uziImages.size
                    )

                    _uiState.update { currentState ->
                        currentState.copy(
                            uziImagesBmp = currentState.uziImagesBmp + (image.id to bitmap)
                        )
                    }
                    println("Изображение ${image.id} добавлено в состояние UI")
                }

                println("Получение сегментов изображений...")
                val uziImageNodesSegments = fetchImageNodesSegments(uziImages)
                println("Сегменты получены: ${uziImageNodesSegments.size} элементов")

                println("Обновление UI после завершения диагностики...")
                updateUiAfterDiagnosticCompletion(
                    diagnosticId,
                    uziImages,
                    uziImageNodesSegments,
                    uziInformation
                )
                println("=== Диагностика успешно завершена ===")

            } catch (e: HttpException) {
                println("=== Ошибка HTTP: ${e.code()} ${e.message} ===")
                e.printStackTrace()
                handleHttpException(e)

            } catch (e: Exception) {
                println("=== Неизвестная ошибка: ${e.javaClass.simpleName} ===")
                e.printStackTrace()
                handleGeneralException(e)
            }
        }
    }

    private fun updateImageDownloadState(currentImage: Int, totalImages: Int) {
        val downloadingProgress = currentImage.toFloat() / totalImages
        updateUploadUziProgressState(DiagnosticProgressStateDetail.DownloadingImages(downloadingProgress))
    }

    private fun updateUploadUziProgressState(progressState: DiagnosticProgressStateDetail) {
        _uiState.update { it.copy(diagnosticProgressStateDetail = progressState) }
        calculateUploadDiagnosticProgress()
    }

    private fun calculateUploadDiagnosticProgress() {
        val diagnosticProgressStateDetail = uiState.value.diagnosticProgressStateDetail
        var progressValue = 0f
        when(diagnosticProgressStateDetail) {
            is DiagnosticProgressStateDetail.UploadingUzi -> progressValue = 0.3f
            is DiagnosticProgressStateDetail.DiagnosticWaiting.New -> progressValue = 0.45f
            is DiagnosticProgressStateDetail.DiagnosticWaiting.Pending -> progressValue = 0.6f
            is DiagnosticProgressStateDetail.DownloadingImages -> {
                progressValue = 0.6f + 0.4f * diagnosticProgressStateDetail.downloadingProgress
            }
        }

        _uiState.update {
            it.copy(
                diagnosticProgressValue = progressValue
            )
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
        updateUploadUziProgressState(DiagnosticProgressStateDetail.UploadingUzi)
        _uiState.update {
            it.copy(
                diagnosticProcessState = DiagnosticProcessState.Sending,
            )
        }
    }

    private suspend fun createDiagnostic(patientId: String): String {
        _uiState.update {
            it.copy(
                diagnosticProcessState = DiagnosticProcessState.Sending,
            )
        }

        val uziId = repository.createUzi(
            uziUris = uiState.value.selectedImageUri!!,
            projection = "long",
            patientId = patientId,
            deviceId = "1",
        ).also {
            println("diagnosticId: $it")
        }
        return uziId
    }

    private suspend fun fetchUziImages(diagnosticId: String): List<UziImage> {
        return repository.getUziImages(diagnosticId)
    }

    private suspend fun fetchImageNodesSegments(uziImages: List<UziImage>): List<NodesSegmentsResponse> =
        coroutineScope {
            val remainingImageNodesSegments = uziImages.map { image ->
                async { fetchImageNodesForSingleImage(image) }
            }.awaitAll()

            remainingImageNodesSegments.filterNotNull()
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
        try {
            uploadDiagnosticJob?.cancel()
            println("Остановил корутину загрузки узи")
        } catch (e: Exception) {
            println("Ошибка при остановке корутины $e")
        }
        _uiState.update {
            it.copy(
                currentScreenIndex = 0,
                selectedImageUri = null,
                selectedImageName = null,
                diagnosticProcessState = DiagnosticProcessState.Failure
            )
        }
        println("Ошибка HTTP: $e")
    }

    private fun handleGeneralException(e: Exception) {
        try {
            uploadDiagnosticJob?.cancel()
            println("Остановил корутину загрузки узи")
        } catch (e: Exception) {
            println("Ошибка при остановке корутины $e")
        }
        _uiState.update {
            it.copy(
                diagnosticProcessState = DiagnosticProcessState.Failure,
            )
        }
        println("Ошибка: $e")
    }

}