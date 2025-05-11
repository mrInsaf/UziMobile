package com.mrinsaf.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
data class User(
    val id: String,
    @SerialName("fullname") val fullName: String,
    val email: String,
    val policy: String,
    val active: Boolean,
    val malignancy: Boolean,
    @SerialName("birth_date") val birthDate: String,
    @SerialName("last_uzi_date") val lastUziDate: String? = null,
)