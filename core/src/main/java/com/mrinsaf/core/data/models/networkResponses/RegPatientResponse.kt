package com.mrinsaf.core.data.models.networkResponses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RegPatientResponse(
    @SerialName("id") val id: String
)
