package com.example.uzi.data.models.networkResponses

data class UziListResponse(
    val uzis: List<Uzi>
)

data class Uzi(
    val id: String,
    val patientId: String,
    val projection: String,
    val createAt: String,
    val deviceId: Int,
    val checked: Boolean
)