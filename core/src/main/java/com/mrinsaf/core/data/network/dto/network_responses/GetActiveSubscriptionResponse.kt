package com.mrinsaf.core.data.network.dto.network_responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetActiveSubscriptionResponse(
    val id: String,

    @SerialName("tariff_plan_id")
    val tariffPlanId: String,

    val status: String,

    @SerialName("start_date")
    val startDate: String,

    @SerialName("end_date")
    val endDate: String
)