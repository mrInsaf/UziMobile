package com.mrinsaf.profile.domain.model

data class PaymentProvider(
    val id: String,
    val name: String,
    val description: String? = null,
    val isActive: Boolean,
)
