package com.mrinsaf.core.domain.model.basic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Segment(
    val id: String,
    val image_id: String,
    val node_id: String,
    val contor: List<SectorPoint>,
    val ai: Boolean,
    @SerialName("tirads_23") val tirads23: Double,
    @SerialName("tirads_4") val tirads4: Double,
    @SerialName("tirads_5") val tirads5: Double
)