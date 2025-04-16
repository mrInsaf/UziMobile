package com.mrinsaf.core.data.models.basic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Uzi(
    @SerialName("id") val id: String,
    @SerialName("external_id") val externalId: String,
    @SerialName("author_id") val authorId: String,
    @SerialName("projection") val projection: String,
    @SerialName("create_at") val createAt: String,
    @SerialName("device_id") val deviceId: Int,
    @SerialName("status") val status: String,
    @SerialName("checked") val checked: Boolean = false
)
