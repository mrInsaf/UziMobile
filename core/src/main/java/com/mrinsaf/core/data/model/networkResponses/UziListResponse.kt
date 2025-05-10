package com.mrinsaf.core.data.model.networkResponses


import com.mrinsaf.core.domain.model.basic.Uzi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UziListResponse(
    @SerialName("uzis") val uzis: List<Uzi>
)