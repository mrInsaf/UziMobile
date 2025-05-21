package com.mrinsaf.subscription.data.repository

import com.mrinsaf.core.data.network.dto.network_request.PurchaseRequest
import com.mrinsaf.core.data.network.dto.network_responses.CheckActiveSubscriptionResponse
import com.mrinsaf.core.data.network.dto.network_responses.GetActiveSubscriptionResponse
import com.mrinsaf.core.data.network.dto.network_responses.PaymentProviderResponse
import com.mrinsaf.core.data.network.dto.network_responses.PurchaseResponse
import com.mrinsaf.core.data.network.dto.network_responses.TariffPlanResponse
import com.mrinsaf.core.domain.model.api_result.ApiResult
import com.mrinsaf.core.domain.repository.SubscriptionRepository
import com.mrinsaf.subscription.data.data_source.SubscriptionApi
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class SubscriptionRepositoryImpl(
    private val subscriptionApi: SubscriptionApi
) : SubscriptionRepository {
    override suspend fun purchaseSubscription(request: PurchaseRequest): ApiResult<PurchaseResponse> {
        return safeApiCall {
            subscriptionApi.purchaseSubscription(request)
        }
    }
    override suspend fun checkActiveSubscription(): ApiResult<CheckActiveSubscriptionResponse> {
        return safeApiCall {
            subscriptionApi.checkActiveSubscription()
        }
    }
    override suspend fun getActiveSubscription(): ApiResult<GetActiveSubscriptionResponse> {
        return safeApiCall {
            subscriptionApi.getActiveSubscription()
        }
    }

    override suspend fun getTariffPlanById(id: String): ApiResult<TariffPlanResponse> {
        return safeApiCall {
            subscriptionApi.getTariffPlanById(id)
        }
    }

    override suspend fun getAllTariffPlans(): ApiResult<List<TariffPlanResponse>> {
        return safeApiCall {
            subscriptionApi.getAllTariffPlans()
        }
    }

    override suspend fun getPaymentProviders(): ApiResult<List<PaymentProviderResponse>> {
        return safeApiCall {
            subscriptionApi.getAllPaymentProviders()
        }
    }

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
        return try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) ApiResult.Success(body)
                else ApiResult.Error.ServerError("Пустой ответ от сервера")
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                ApiResult.Error.ServerError("Server error: $errorBody")
            }
        } catch (e: HttpException) {
            ApiResult.Error.ServerError(e.message ?: "Неизвестная ошибка сервера")
        } catch (e: IOException) {
            ApiResult.Error.NetworkError(e.message ?: "Неизвестная сетевая ошибка")
        } catch (e: Exception) {
            ApiResult.Error.UnknownError
        }
    }
}