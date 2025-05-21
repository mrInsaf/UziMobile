package com.mrinsaf.core.domain.repository

import com.mrinsaf.core.data.network.dto.network_request.PurchaseRequest
import com.mrinsaf.core.data.network.dto.network_responses.GetActiveSubscriptionResponse
import com.mrinsaf.core.data.network.dto.network_responses.PaymentProviderResponse
import com.mrinsaf.core.data.network.dto.network_responses.PurchaseResponse
import com.mrinsaf.core.data.network.dto.network_responses.TariffPlanResponse

interface SubscriptionRepository {
    suspend fun purchaseSubscription(request: PurchaseRequest): Result<PurchaseResponse>
    suspend fun checkActiveSubscription(): Result<Boolean>
    suspend fun getActiveSubscription(): Result<GetActiveSubscriptionResponse>

    suspend fun getTariffPlanById(id: String): Result<TariffPlanResponse>
    suspend fun getAllTariffPlans(): Result<List<TariffPlanResponse>>

    suspend fun getPaymentProviders(): Result<List<PaymentProviderResponse>>
}