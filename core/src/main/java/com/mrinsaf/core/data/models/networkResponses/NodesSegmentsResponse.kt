package com.mrinsaf.core.data.models.networkResponses

import com.mrinsaf.core.data.models.basic.Node
import com.mrinsaf.core.data.models.basic.Segment
import kotlinx.serialization.Serializable


@Serializable
data class NodesSegmentsResponse(
    val nodes: List<Node>,
    val segments: List<Segment>
)



