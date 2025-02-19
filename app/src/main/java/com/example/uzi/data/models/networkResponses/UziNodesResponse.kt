package com.example.uzi.data.models.networkResponses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UziNodesResponse(
    val nodes: List<UziNode>
)

@Serializable
data class UziNode(
    val ai: Boolean,
    val id: String,
    @SerialName("tirads_23") val tirads23: Float,
    @SerialName("tirads_4") val tirads4: Float,
    @SerialName("tirads_5") val tirads5: Float
)