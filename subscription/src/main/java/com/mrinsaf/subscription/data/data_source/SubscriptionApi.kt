package com.mrinsaf.subscription.data.data_source

import com.mrinsaf.core.data.network.dto.network_request.PurchaseRequest
import com.mrinsaf.core.data.network.dto.network_responses.CheckActiveSubscriptionResponse
import com.mrinsaf.core.data.network.dto.network_responses.GetActiveSubscriptionResponse
import com.mrinsaf.core.data.network.dto.network_responses.PaymentProviderResponse
import com.mrinsaf.core.data.network.dto.network_responses.PurchaseResponse
import com.mrinsaf.core.data.network.dto.network_responses.TariffPlanResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface SubscriptionApi {
    @POST("/subscriptions/purchase")
    suspend fun purchaseSubscription(@Body request: PurchaseRequest): Response<PurchaseResponse>

    @GET("/subscriptions/check-active")
    suspend fun checkActiveSubscription(): Response<CheckActiveSubscriptionResponse>

    @GET("/subscriptions/get-active")
    suspend fun getActiveSubscription(): Response<GetActiveSubscriptionResponse>

    @GET("/tariff_plans/{id}")
    suspend fun getTariffPlanById(@Path("id") id: String): Response<TariffPlanResponse>

    @GET("/tariff_plans")
    suspend fun getAllTariffPlans(): Response<List<TariffPlanResponse>>

    @GET("/payment_providers")
    suspend fun getAllPaymentProviders(): Response<List<PaymentProviderResponse>>
}