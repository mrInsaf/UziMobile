package com.mrinsaf.core.data.models.networkResponses

import com.mrinsaf.core.data.models.basic.Node
import kotlinx.serialization.Serializable

@Serializable
data class UziNodesResponse(
    val nodes: List<Node>
)
