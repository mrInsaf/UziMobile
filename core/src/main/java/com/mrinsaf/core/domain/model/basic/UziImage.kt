package com.mrinsaf.core.domain.model.basic

import kotlinx.serialization.Serializable

@Serializable
data class UziImage(
    val id: String,
    val page: Int
)