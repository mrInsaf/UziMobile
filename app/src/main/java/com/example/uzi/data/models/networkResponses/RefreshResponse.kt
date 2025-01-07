package com.example.uzi.data.models.networkResponses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshResponse(
    @SerialName("access_key") val accessKey: String,
    @SerialName("refresh_key") val refreshKey: String
)