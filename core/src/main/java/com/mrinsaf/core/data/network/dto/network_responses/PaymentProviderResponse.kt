package com.mrinsaf.core.data.network.dto.network_responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentProviderResponse(
    val id: String,
    val name: String,
    @SerialName("is_active")
    val isActive: Boolean
)