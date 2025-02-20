package com.example.uzi.data.models.networkResponses

import com.example.uzi.data.models.basic.Uzi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UziListResponse(
    @SerialName("uzis") val uzis: List<Uzi>
)