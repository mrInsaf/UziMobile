package com.mrinsaf.core.data.model.network_responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName(value = "access_token")
    val accessToken: String,
    @SerialName(value = "refresh_token")
    val refreshToken: String
)