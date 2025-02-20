package com.example.uzi.data.models.basic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Node(
    val id: String,
    val ai: Boolean,
    @SerialName("tirads_23") val tirads23: Double,
    @SerialName("tirads_4") val tirads4: Double,
    @SerialName("tirads_5") val tirads5: Double
) {
    val maxTirads: Double
        get() = maxOf(
            tirads23,
            tirads4,
            tirads5
        )

    val formationClass: Int
        get() = when (maxTirads) {
            tirads23 -> 2
            tirads4 -> 4
            tirads5 -> 5
            else -> 0
        }
}
