package com.mrinsaf.core.domain.model.basic

import kotlinx.serialization.Serializable

@Serializable
data class SectorPoint(
    val x: Int,
    val y : Int,
)
