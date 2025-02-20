package com.example.uzi.data.models.networkResponses

import com.example.uzi.data.models.basic.Node
import kotlinx.serialization.Serializable

@Serializable
data class UziNodesResponse(
    val nodes: List<Node>
)
