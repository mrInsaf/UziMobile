package com.mrinsaf.core.data.network.dto.network_responses


import com.mrinsaf.core.domain.model.basic.Uzi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UziListResponse(
    @SerialName("uzis") val uzis: List<Uzi>
)