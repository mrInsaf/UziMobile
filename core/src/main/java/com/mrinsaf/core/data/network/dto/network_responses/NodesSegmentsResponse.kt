package com.mrinsaf.core.data.network.dto.network_responses

import com.mrinsaf.core.domain.model.basic.Node
import com.mrinsaf.core.domain.model.basic.Segment
import kotlinx.serialization.Serializable


@Serializable
data class NodesSegmentsResponse(
    val nodes: List<Node>,
    val segments: List<Segment>
)



