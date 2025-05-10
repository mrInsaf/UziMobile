package com.mrinsaf.core.data.model.networkResponses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RegPatientResponse(
    @SerialName("id") val id: String
)
