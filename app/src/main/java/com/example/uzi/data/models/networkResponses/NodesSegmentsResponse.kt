package com.example.uzi.data.models.networkResponses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


@Serializable
data class NodesSegmentsResponse(
    val nodes: List<Node>,
    val segments: List<Segment>
)

@Serializable
data class Node(
    val id: String,
    val ai: Boolean,
    @SerialName("tirads_23") val tirads23: Double,
    @SerialName("tirads_4") val tirads4: Double,
    @SerialName("tirads_5") val tirads5: Double
) {
    // Вычисляемое свойство для максимального TIRADS
    val maxTirads: Double
        get() = maxOf(
            tirads23,
            tirads4,
            tirads5
        )

    // Вычисляемое свойство для класса формации
    val formationClass: Int
        get() = when (maxTirads) {
            tirads23 -> 2 // TIRADS 2-3
            tirads4 -> 4 // TIRADS 4
            tirads5 -> 5 // TIRADS 5
            else -> 0 // На случай ошибки
        }
}

@Serializable
data class Segment(
    val id: String,
    val node_id: String,
    val image_id: String,
    val contor: String,
    @SerialName("tirads_23") val tirads23: Double,
    @SerialName("tirads_4") val tirads4: Double,
    @SerialName("tirads_5") val tirads5: Double
) {
    fun getContorPoints(): List<SectorPoint> {
        return Json.decodeFromString(contor)
    }
}

