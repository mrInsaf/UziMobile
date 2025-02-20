package com.mrinsaf.core.data.models.networkRequests

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)