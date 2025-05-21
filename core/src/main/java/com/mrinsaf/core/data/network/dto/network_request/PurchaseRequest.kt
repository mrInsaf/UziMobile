package com.mrinsaf.core.data.network.dto.network_request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseRequest(
    @SerialName("tariff_plan_id")
    val tariffPlanId: String,

    @SerialName("payment_provider_id")
    val paymentProviderId: String
)