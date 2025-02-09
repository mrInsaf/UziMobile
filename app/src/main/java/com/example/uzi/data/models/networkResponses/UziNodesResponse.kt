package com.example.uzi.data.models.networkResponses

import kotlinx.serialization.Serializable

@Serializable
data class UziNodesResponse(
    val nodes: List<UziNode>
)

@Serializable
data class UziNode(
    val ai: Boolean,
    val id: String,
    val tirads23: Int,
    val tirads4: Int,
    val tirads5: Int
)