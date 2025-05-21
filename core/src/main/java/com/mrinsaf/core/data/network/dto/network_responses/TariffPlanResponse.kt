package com.mrinsaf.core.data.network.dto.network_responses

import kotlinx.serialization.Serializable

@Serializable
data class TariffPlanResponse(
    val id: String,
    val name: String,
    val description: String,
    val price: String,
    val duration: Long
)