package com.example.uzi.data.models.networkResponses

import kotlinx.serialization.Serializable

@Serializable
data class UziImage(
    val id: String,
    val page: Int
)