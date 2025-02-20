package com.mrinsaf.core.data.models.networkResponses


import com.mrinsaf.core.data.models.basic.Uzi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UziListResponse(
    @SerialName("uzis") val uzis: List<Uzi>
)