package com.mrinsaf.core.data.models.basic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Uzi(
    @SerialName("id") val id: String,
    @SerialName("patient_id") val patientId: String,
    @SerialName("projection") val projection: String,
    @SerialName("create_at") val createAt: String,
    @SerialName("device_id") val deviceId: Int,
    @SerialName("checked") val checked: Boolean = false
)
