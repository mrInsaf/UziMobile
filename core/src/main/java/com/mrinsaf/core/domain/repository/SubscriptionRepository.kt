package com.mrinsaf.core.domain.repository

import com.mrinsaf.core.data.network.dto.network_request.PurchaseRequest
import com.mrinsaf.core.data.network.dto.network_responses.CheckActiveSubscriptionResponse
import com.mrinsaf.core.data.network.dto.network_responses.GetActiveSubscriptionResponse
import com.mrinsaf.core.data.network.dto.network_responses.PaymentProviderResponse
import com.mrinsaf.core.data.network.dto.network_responses.PurchaseResponse
import com.mrinsaf.core.data.network.dto.network_responses.TariffPlanResponse
import com.mrinsaf.core.domain.model.api_result.ApiResult

interface SubscriptionRepository {
    suspend fun purchaseSubscription(request: PurchaseRequest): ApiResult<PurchaseResponse>
    suspend fun checkActiveSubscription(): ApiResult<CheckActiveSubscriptionResponse>
    suspend fun getActiveSubscription(): ApiResult<GetActiveSubscriptionResponse>

    suspend fun getTariffPlanById(id: String): ApiResult<TariffPlanResponse>
    suspend fun getAllTariffPlans(): ApiResult<List<TariffPlanResponse>>

    suspend fun getPaymentProviders(): ApiResult<List<PaymentProviderResponse>>
}