package com.example.uzi.data.models.networkResponses

import com.example.uzi.data.models.basic.Node
import com.example.uzi.data.models.basic.Segment
import kotlinx.serialization.Serializable


@Serializable
data class NodesSegmentsResponse(
    val nodes: List<Node>,
    val segments: List<Segment>
)



