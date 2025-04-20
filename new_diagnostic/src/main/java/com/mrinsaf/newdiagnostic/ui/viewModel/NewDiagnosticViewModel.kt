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
        println(fileName)
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
                println("=== Начало диагностики ===")

                updateUiBeforeDiagnosticStart()
                println("UI обновлен перед стартом диагностики")

                println("Создание диагностической записи...")
                val diagnosticId = createDiagnostic().also {
                    println("Diagnostic ID получен: $it")
                    _uiState.update { state ->
                        state.copy(isUziPosted = true)
                    }
                }

//                 val diagnosticId = "d5b076a2-2881-43ee-b308-86f686ff9471"
                println("Используется Diagnostic ID: $diagnosticId")

                println("Получение информации о УЗИ...")
                val uziInformation = repository.getUzi(diagnosticId)
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
                selectedImageUri = null,
                selectedImageName = null,
            )
        }
        println("Ошибка HTTP: $e")
    }

    private fun handleGeneralException(e: Exception) {
        println("Ошибка: $e")
    }

}