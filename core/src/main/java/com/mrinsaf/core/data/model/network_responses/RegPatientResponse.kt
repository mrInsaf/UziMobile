package com.mrinsaf.core.data.model.network_responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RegPatientResponse(
    @SerialName("id") val id: String
)
