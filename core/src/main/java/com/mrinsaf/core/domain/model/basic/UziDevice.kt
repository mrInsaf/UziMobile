package com.mrinsaf.core.domain.model.basic

import kotlinx.serialization.Serializable

@Serializable
data class UziDevice(
    val id: Int,
    val name: String
)