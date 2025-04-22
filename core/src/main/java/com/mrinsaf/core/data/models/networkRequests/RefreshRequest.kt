package com.mrinsaf.core.data.models.networkRequests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(
    @SerialName("refresh_token") val refreshToken: String
)