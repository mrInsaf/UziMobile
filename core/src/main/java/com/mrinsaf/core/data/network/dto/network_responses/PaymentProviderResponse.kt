package com.mrinsaf.core.data.network.dto.network_responses

import kotlinx.serialization.Serializable

@Serializable
data class PaymentProviderResponse(
    val id: String,
    val name: String,
    val is_active: Boolean
)