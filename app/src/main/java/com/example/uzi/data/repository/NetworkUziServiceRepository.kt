package com.example.uzi.data.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.uzi.data.repository.local.TokenStorage
import com.example.uzi.data.models.networkResponses.LoginResponse
import com.example.uzi.data.models.networkResponses.ReportResponse
import com.example.uzi.data.models.User
import com.example.uzi.data.models.networkRequests.LoginRequest
import com.example.uzi.data.models.networkResponses.NodesSegmentsResponse
import com.example.uzi.data.models.networkResponses.Uzi
import com.example.uzi.data.models.networkResponses.UziImage
import com.example.uzi.data.repository.local.CacheFileUtil
import com.example.uzi.data.network.UziApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.File
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class NetworkUziServiceRepository(
    private val uziApiService: UziApiService,
    private val context: Context
): UziServiceRepository {
    override suspend fun checkAuthorisation(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun submitLogin(email: String, password: String): LoginResponse {
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
        uziUris: List<Uri>,
        projection: String,
        patientId: String,
        deviceId: String
    ): String {
        println("я в репозитории")
        return safeApiCall { accessToken ->
            println("получаю юри")
            val uziFile = File(getRealPathFromURI(context, uziUris.first())) // TODO: Добавить обработку нескольких файлов
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

    override suspend fun saveUziImageAndGetCacheUri(uziId: String, imageId: String): Uri {
        println("Сохраняю картинку $imageId")
        val responseBody = downloadUziImage(uziId, imageId)
        val fileName = "$uziId-$imageId.jpg"
        return CacheFileUtil.saveFileToCache(context, fileName, responseBody)
            ?: throw Exception("Не удалось сохранить файл в кэш.")
    }

    override suspend fun downloadUziFile(uziId: String): ResponseBody {
        return retryWithHandling(maxAttempts = 10, delayMillis = 2000L) { accessToken ->
            val response = uziApiService.downloadUzi(accessToken, uziId)

            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw Exception("Ошибка запроса: ${response.code()} ${response.message()}")
            }
        }
    }

    override suspend fun saveUziFileAndGetCacheUri(uziId: String, responseBody: ResponseBody): Uri {
        val contentType = responseBody.contentType()?.toString()
            ?: throw Exception("Невозможно определить MIME-тип файла")

        println("contentType: $contentType")

        val extension = when {
            contentType.contains("tiff", ignoreCase = true) -> "tiff"
            contentType.contains("png", ignoreCase = true) -> "png"
            else -> throw Exception("Неизвестный формат файла: $contentType")
        }

        println("Определено расширение: $extension")
        val fileName = "$uziId.$extension"
        println("Сохраняем файл с именем: $fileName")

        val file = CacheFileUtil.saveFileToCache(context, fileName, responseBody)
            ?: throw Exception("Не удалось сохранить УЗИ в кэш")

        println("Файл успешно сохранен: ${file.path}, размер: ${File(file.path).length()} байт")
        return file
    }

    @SuppressLint("NewApi")
    private fun parseDate(dateString: String): ZonedDateTime {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        return ZonedDateTime.parse(dateString, formatter)
    }

    override suspend fun getUziList(patientId: String): List<Uzi> {
        return safeApiCall { accessToken ->
            uziApiService.getPatientUzis(accessToken, patientId)
        }.uzis.map { uzi ->
            uzi.copy(
                createAt = parseDate(uzi.createAt).toString()
            )
        }
    }


    override suspend fun submitLogout(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(): User {
        TODO("Not yet implemented")
    }


    override suspend fun getUziById(uziId: String): ReportResponse {
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
                // Обработка ошибки 403
                if (e.code() == 403) {
                    throw Exception("Ошибка 403: Доступ запрещен.")
                }
                println("Попытка ${attempt + 1}: Ошибка - ${e.message()}")
            } catch (e: Exception) {
                println("Попытка ${attempt + 1}: Ошибка - ${e.message}")
            }

            delay(delayMillis)
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
                    throw refreshException
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

