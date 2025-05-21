package com.mrinsaf.subscription.data.repository

import com.mrinsaf.core.data.network.dto.network_request.PurchaseRequest
import com.mrinsaf.core.data.network.dto.network_responses.GetActiveSubscriptionResponse
import com.mrinsaf.core.data.network.dto.network_responses.PaymentProviderResponse
import com.mrinsaf.core.data.network.dto.network_responses.PurchaseResponse
import com.mrinsaf.core.data.network.dto.network_responses.TariffPlanResponse
import com.mrinsaf.core.domain.repository.SubscriptionRepository
import com.mrinsaf.subscription.data.data_source.SubscriptionApi
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class SubscriptionRepositoryImpl(
    private val subscriptionApi: SubscriptionApi
) : SubscriptionRepository {
    override suspend fun purchaseSubscription(request: PurchaseRequest): Result<PurchaseResponse> {
        return safeApiCall {
            subscriptionApi.purchaseSubscription(request)
        }
    }
    override suspend fun checkActiveSubscription(): Result<Boolean> {
        return safeApiCall {
            subscriptionApi.checkActiveSubscription()
        }.map { response ->
            response.hasActiveSubscription
        }
    }
    override suspend fun getActiveSubscription(): Result<GetActiveSubscriptionResponse> {
        return safeApiCall {
            subscriptionApi.getActiveSubscription()
        }
    }

    override suspend fun getTariffPlanById(id: String): Result<TariffPlanResponse> {
        return safeApiCall {
            subscriptionApi.getTariffPlanById(id)
        }
    }

    override suspend fun getAllTariffPlans(): Result<List<TariffPlanResponse>> {
        return safeApiCall {
            subscriptionApi.getAllTariffPlans()
        }
    }

    override suspend fun getPaymentProviders(): Result<List<PaymentProviderResponse>> {
        return safeApiCall {
            subscriptionApi.getAllPaymentProviders()
        }
    }

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
        return try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) Result.success(body)
                else Result.failure(Exception("Empty response body"))
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("Server error: $errorBody"))
            }
        } catch (e: HttpException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: IOException) {
            Result.failure(Exception("No internet connection: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.message}"))
        }
    }
}