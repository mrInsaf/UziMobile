package com.mrinsaf.core.data.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.annotation.RequiresApi
import com.mrinsaf.core.data.models.basic.Uzi
import com.mrinsaf.core.data.models.basic.UziImage
import com.mrinsaf.core.data.models.networkRequests.LoginRequest
import com.mrinsaf.core.data.models.networkResponses.NodesSegmentsResponse
import com.mrinsaf.core.data.models.networkResponses.UziNodesResponse
import com.mrinsaf.core.data.network.UziApiService
import com.mrinsaf.core.data.repository.local.FileStorage
import com.mrinsaf.core.data.repository.local.TokenStorage
import com.mrinsaf.core.ui.UiEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.File
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class NetworkUziServiceRepository(
    private val uziApiService: UziApiService,
    private val context: Context
): UziServiceRepository {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    override suspend fun checkAuthorisation(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun submitLogin(email: String, password: String): com.mrinsaf.core.data.models.networkResponses.LoginResponse {
        val loginResponse = uziApiService.login(
            request = LoginRequest(
                email = email,
                password = password
            )
        )
        loginResponse.accessToken.let {
            TokenStorage.saveAccessToken(context, it)
        }
        loginResponse.refreshToken.let {
            TokenStorage.saveRefreshToken(context, it)
        }
        return loginResponse
    }

    override suspend fun refreshToken() {
        println("Рефрешу токен")
        val refreshToken = TokenStorage.getRefreshToken(context).first()

        if (refreshToken != null) {
            val response = uziApiService.refreshToken(refreshToken)

            response.accessKey.let {
                TokenStorage.saveAccessToken(context, it)
            }
            response.refreshKey.let {
                TokenStorage.saveRefreshToken(context, it)
            }
        } else {
            throw TokenNotFoundException("Refresh token not found")
        }
    }

    override suspend fun createUzi(
        uziUris: Uri,
        projection: String,
        patientId: String,
        deviceId: String
    ): String {
        println("я в репозитории")
        return safeApiCall { accessToken ->
            println("получаю юри")
            val uziFile = File(getRealPathFromURI(context, uziUris)) // TODO: Добавить обработку нескольких файлов
            println("получил юри")
            val requestFile = uziFile.asRequestBody("image/*".toMediaTypeOrNull())
            val uziFilePart = MultipartBody.Part.createFormData("file", uziFile.name, requestFile)

            val projectionRequestBody = projection.toRequestBody(MultipartBody.FORM)
            val patientIdRequestBody = patientId.toRequestBody(MultipartBody.FORM)
            val deviceIdRequestBody = deviceId.toRequestBody(MultipartBody.FORM)
            println("отправляю узи через апи ")
            val response = uziApiService.createUzi(
                accessToken = accessToken,
                uziFile = uziFilePart,
                projection = projectionRequestBody,
                patientId = patientIdRequestBody,
                deviceId = deviceIdRequestBody
            )

            if (response.isSuccessful) {
                response.body() ?: "Success"
            } else {
                // Если ошибка 403, выбрасываем HttpException
                if (response.code() == 403) {
                    throw HttpException(response)
                }
                // Для других ошибок выбрасываем обычное исключение
                throw Exception("Error: ${response.code()} - ${response.message()}")
            }
        }
    }

    override suspend fun getUziImages(uziId: String): List<UziImage> {
        return retryWithHandling(maxAttempts = 50, delayMillis = 5000L) { accessToken ->
            val result = uziApiService.getUziImages(
                accessToken = accessToken,
                uziId = uziId
            )

            if (!result.isNullOrEmpty()) {
                result
            } else {
                throw Exception("Пустой результат для изображения с ID: $uziId")
            }
        }
    }

    override suspend fun getImageNodesAndSegments(imageId: String, diagnosticCompleted: Boolean): NodesSegmentsResponse {
        val maxAttempts = if (diagnosticCompleted) 1 else 50
        val delayMillis = 5000L

        return retryWithHandling(maxAttempts = maxAttempts, delayMillis = delayMillis) { accessToken ->
            val response = uziApiService.getImageNodesAndSegments(accessToken, imageId)

            if (response.nodes.isNotEmpty() && response.segments.isNotEmpty()) {
                response
            } else {
                throw Exception("Получен пустой ответ для изображения с ID: $imageId")
            }
        }
    }

    override suspend fun downloadUziImage(uziId: String, imageId: String): ResponseBody {
        return retryWithHandling(maxAttempts = 3, delayMillis = 2000L) { accessToken ->
            val response = uziApiService.downloadUziImage(accessToken, uziId, imageId)

            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw Exception("Ошибка запроса: ${response.code()} ${response.message()}")
            }
        }
    }

//    override suspend fun saveUziImageAndGetCacheUri(uziId: String, imageId: String): Uri {
//        println("Сохраняю картинку $imageId")
//        val responseBody = downloadUziImage(uziId, imageId)
//        val fileName = "$uziId-$imageId.jpg"
//        return FileStorage.saveFileToStorage(context, fileName, responseBody)
//            ?: throw Exception("Не удалось сохранить файл в кэш.")
//    }

    override suspend fun downloadUziFile(uziId: String): ResponseBody {
        return retryWithHandling(maxAttempts = 10, delayMillis = 2000L) { accessToken ->
            val response = uziApiService.downloadUzi(accessToken, uziId)
            println(response.body())

            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw Exception("Ошибка запроса: ${response.code()} ${response.message()}")
            }
        }
    }

    override suspend fun saveUziFileAndGetCacheUri(uziId: String, responseBody: ResponseBody): Uri {
        // 1. Проверка наличия контента
        if (responseBody.contentLength() == 0L) {
            throw Exception("Получен пустой файл")
        }

        // 2. Определение MIME-типа
        val contentType = responseBody.contentType()?.toString()
            ?: throw Exception("Невозможно определить MIME-тип файла")

        // 3. Определение расширения
        val extension = when {
            contentType.contains("tiff", true) -> "tiff"
            contentType.contains("png", true) -> "png"
            else -> throw Exception("Неизвестный формат: $contentType")
        }

        val fileName = "$uziId.$extension"
        println("fileName: $fileName")

        // 4. Сохранение с проверкой записи
        val fileUri = FileStorage.saveResponseBodyToStorage(context, fileName, responseBody)
            ?: throw Exception("Ошибка сохранения файла")

        // 5. Валидация результата
        val cachedFile = File(fileUri.path?.let { Uri.decode(it) } ?: "")
        if (!cachedFile.exists() || cachedFile.length() == 0L) {
            cachedFile.delete()
            throw Exception("Файл не был записан на диск")
        }

        return fileUri
    }

    @SuppressLint("NewApi")
    private fun parseDate(dateString: String): String {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val date = ZonedDateTime.parse(dateString, formatter)
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }


    override suspend fun getPatientUzis(patientId: String): List<Uzi> {
        return safeApiCall { accessToken ->
            uziApiService.getPatientUzis(accessToken, patientId)
        }.uzis.map { uzi ->
            uzi.copy(
                createAt = parseDate(uzi.createAt).toString()
            )
        }
//            .also { println(it) }
    }

    override suspend fun getUziNodes(uziId: String): UziNodesResponse {
        return safeApiCall { accessToken ->
            uziApiService.getUziNodes(uziId, accessToken)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getUzi(uziId: String): Uzi {
        return safeApiCall { token ->
            val uzi = uziApiService.getUzi(token, uziId)
            uzi.copy(createAt = formatDate(uzi.createAt)) // Преобразуем дату перед возвратом
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDate(isoDate: String): String {
        return LocalDate.parse(isoDate.substringBefore("T"))
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }


    override suspend fun submitLogout(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(): com.mrinsaf.core.data.models.User {
        TODO("Not yet implemented")
    }

    private suspend fun <T> retryWithHandling(
        maxAttempts: Int,
        delayMillis: Long,
        apiCall: suspend (String) -> T
    ): T {
        repeat(maxAttempts) { attempt ->
            try {
                return safeApiCall(apiCall)
            } catch (e: HttpException) {
                println(e)
                // Обработка ошибки 403
                if (e.code() == 403) {
                    println("Ошибка 403: Доступ запрещен")
                    throw Exception("Ошибка 403: Доступ запрещен.")
                }
                println("Попытка ${attempt + 1}: Ошибка - ${e.message()}")
            } catch (e: Exception) {
                println("Попытка ${attempt + 1}: Ошибка - ${e.message}")
            }

            if (maxAttempts > 1){
                println("Ожидаю ....")
                delay(delayMillis)
            }
        }

        throw Exception("Не удалось выполнить запрос после $maxAttempts попыток.")
    }


    private suspend fun <T> safeApiCall(
        apiCall: suspend (String) -> T
    ): T {
        val accessToken = try {
            TokenStorage.getAccessToken(context).firstOrNull()
        } catch (e: Exception) {
            println(e)
            throw e
        }

        requireNotNull(accessToken) { "Access token is missing" }

        return try {
            apiCall(accessToken)
        } catch (e: HttpException) {
            if (e.code() == 401 || e.code() == 403) {
                println("Похоже токен истек...")
                val refreshToken = TokenStorage.getRefreshToken(context).firstOrNull()

                requireNotNull(refreshToken) { "Refresh token is missing" }

                val newTokens = try {
                    println("Запросил новый токен...")
                    val refreshResponse = uziApiService.refreshToken(refreshToken)

                    TokenStorage.saveAccessToken(context, refreshResponse.accessKey)
                    TokenStorage.saveRefreshToken(context, refreshResponse.refreshKey)

                    refreshResponse.accessKey
                } catch (refreshException: Exception) {
                    println("Ошибка при обновлении токена: ${refreshException.message}")
                    TokenStorage.clearTokens(context)
                    onTokenExpiration()
                    ""
                }

                return apiCall(newTokens)
            } else {
                println("Ошибка запроса: ${e.message}")
                throw e
            }
        } catch (e: Exception) {
            println("Не HTTP ошибка: ${e::class.java.simpleName} - ${e.message}")
            throw e
        }
    }

    private fun onTokenExpiration() {
        println("Все пзц")
        scope.launch {
            _uiEvent.emit(UiEvent.ShowToast("Сессия истекла"))
        }
    }

    private fun getRealPathFromURI(context: Context, uri: Uri): String? {
        try {
            val contentResolver = context.contentResolver
            val fileName = getFileName(contentResolver, uri) // Получаем имя файла

            // Сохраняем файл во внутреннее хранилище
            val file = File(context.cacheDir, fileName)
            contentResolver.openInputStream(uri)?.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            return file.absolutePath // Возвращаем путь до сохраненного файла
        } catch (e: Exception) {
            println(e)
            throw e
        }
    }

    // Получаем имя файла из URI
    private fun getFileName(contentResolver: ContentResolver, uri: Uri): String {
        var name = "temp_file"
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && it.moveToFirst()) {
                name = it.getString(nameIndex)
            }
        }
        return name
    }

    class TokenNotFoundException(message: String) : Exception(message)
}

