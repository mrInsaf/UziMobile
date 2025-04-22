package com.mrinsaf.core.data.models.basic

import kotlinx.serialization.Serializable

@Serializable
data class UziDevice(
    val id: Int,
    val name: String
)