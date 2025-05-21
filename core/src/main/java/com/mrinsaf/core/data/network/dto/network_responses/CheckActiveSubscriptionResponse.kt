package com.mrinsaf.core.data.network.dto.network_responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckActiveSubscriptionResponse(
    @SerialName("has_active_subscription")
    val hasActiveSubscription: Boolean
)