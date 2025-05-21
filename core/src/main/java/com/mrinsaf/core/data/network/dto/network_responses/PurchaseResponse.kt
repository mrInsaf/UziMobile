package com.mrinsaf.core.data.network.dto.network_responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseResponse(
    @SerialName("subscription_id")
    val subscriptionId: String,

    @SerialName("confirmation_url")
    val confirmationUrl: String
)