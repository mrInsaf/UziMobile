package com.mrinsaf.profile.domain.model

data class ActiveSubscription(
    val tariffName: String,
    val daysUntilExpiration: Int,
)
