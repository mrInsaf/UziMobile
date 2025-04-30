package com.mrinsaf.core.data.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.annotation.RequiresApi
import com.mrinsaf.core.data.models.basic.Node
import com.mrinsaf.core.data.models.basic.Uzi
import com.mrinsaf.core.data.models.basic.UziDevice
import com.mrinsaf.core.data.models.basic.UziImage
import com.mrinsaf.core.data.models.networkRequests.LoginRequest
import com.mrinsaf.core.data.models.networkRequests.RefreshRequest
import com.mrinsaf.core.data.models.networkRequests.RegPatientRequest
import com.mrinsaf.core.data.models.networkResponses.NodesSegmentsResponse
import com.mrinsaf.core.data.models.networkResponses.RegPatientResponse
import com.mrinsaf.core.data.network.AuthApiService
import com.mrinsaf.core.data.network.UziApiService
import com.mrinsaf.core.data.repository.local.TokenStorage
import kotlinx.coroutines.delay
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
    private val authApiService: AuthApiService,
    private val context: Context
): UziServiceRepository {
    override suspend fun checkAuthorisation(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun submitLogin(email: String, password: String): com.mrinsaf.core.data.models.networkResponses.LoginResponse {
        val loginResponse = authApiService.login(
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
        val refreshToken = TokenStorage.refreshToken.value

        if (refreshToken != null) {
            val response = authApiService.refreshToken(RefreshRequest(refreshToken))

            response.accessToken.let {
                TokenStorage.saveAccessToken(context, it)
            }

            response.refreshToken.let {
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
        return safeApiCall {
            println("получаю юри")
            val uziFile = File(getRealPathFromURI(context, uziUris))
            println("получил юри")
            val requestFile = uziFile.asRequestBody("image/tiff".toMediaTypeOrNull())
            val uziFilePart = MultipartBody.Part.createFormData("file", uziFile.name, requestFile)

            val projectionRequestBody = projection.toRequestBody(MultipartBody.FORM)
            val patientIdRequestBody = patientId.toRequestBody(MultipartBody.FORM)
            val deviceIdRequestBody = deviceId.toRequestBody(MultipartBody.FORM)
            println("отправляю узи через апи ")
            val response = uziApiService.createUzi(
                uziFile = uziFilePart,
                projection = projectionRequestBody,
                externalId = patientIdRequestBody,
                deviceId = deviceIdRequestBody
            )

            if (response.isSuccessful) {
                println("$response, ${response.body()}")
                response.body()?.id ?: "Success"
            } else {
                throw HttpException(response)
            }
        }
    }

    override suspend fun getUziImages(uziId: String): List<UziImage> {
        return retryWithHandling(maxAttempts = 50, delayMillis = 5000L) {
            val result = uziApiService.getUziImages(uziId = uziId)

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

        return retryWithHandling(maxAttempts = maxAttempts, delayMillis = delayMillis) {
            val response = uziApiService.getImageNodesAndSegments(imageId)

            if (response.nodes.isNotEmpty() && response.segments.isNotEmpty()) {
                response
            } else {
                throw Exception("Получен пустой ответ для изображения с ID: $imageId")
            }
        }
    }

    override suspend fun downloadUziImage(uziId: String, imageId: String): ResponseBody {
        return retryWithHandling(maxAttempts = 3, delayMillis = 2000L) {
            val response = uziApiService.downloadUziImage(uziId, imageId)

            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                throw Exception("Ошибка запроса: ${response.code()} ${response.message()}")
            }
        }
    }

    @SuppressLint("NewApi")
    private fun parseDate(dateString: String): String {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val date = ZonedDateTime.parse(dateString, formatter)
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }


    override suspend fun getPatientUzis(patientId: String): List<Uzi> {
        return safeApiCall {
            uziApiService.getUzisByExternalId(patientId)
        }.map { uzi ->
            uzi.copy(
                createAt = parseDate(uzi.createAt).toString()
            )
        }
    }

    override suspend fun getUziNodes(uziId: String): List<Node> {
        return safeApiCall {
            uziApiService.getUziNodes(uziId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getUzi(uziId: String): Uzi {
        return safeApiCall {
            val uzi = uziApiService.getUzi(uziId)
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

    override suspend fun regPatient(request: RegPatientRequest): RegPatientResponse {
        return authApiService.regPatient(request).body()!!
    }

    override suspend fun getUziDevices(): List<UziDevice> {
        return try {
            val response = uziApiService.getUziDevices()
            response.body() ?: emptyList()
        } catch (e: Exception) {
            println("Ошибка сети: ${e.message}")
            throw e
        }
    }

    private suspend fun <T> retryWithHandling(
        maxAttempts: Int,
        delayMillis: Long,
        apiCall: suspend () -> T
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
        apiCall: suspend () -> T
    ): T {
        return try {
            apiCall()
        } catch (e: HttpException) {
            println("http ошибка")
            throw e
        } catch (e: Exception) {
            println("Не http ошибка")
            println("Ошибка: ${e.javaClass.simpleName}")
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
