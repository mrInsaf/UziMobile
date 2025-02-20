package com.example.uzi.data.models.basic

import kotlinx.serialization.Serializable

@Serializable
data class UziImage(
    val id: String,
    val page: Int
)