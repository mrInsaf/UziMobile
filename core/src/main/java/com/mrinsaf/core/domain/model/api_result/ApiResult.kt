package com.mrinsaf.core.domain.model.api_result

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    sealed class Error(val message: String): ApiResult<Nothing>() {
        data class NetworkError(val networkErrorMessage: String) : Error(
            "Нет подключения к интернету: $networkErrorMessage"
        )

        data class ServerError(val serverErrorMessage: String) : Error(serverErrorMessage)

        object UnknownError : Error("Неизвестная ошибка")
    }
}

