package com.mrinsaf.core.data.network.dto.network_request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)