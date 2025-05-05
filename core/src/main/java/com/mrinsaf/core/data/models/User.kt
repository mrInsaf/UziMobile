package com.mrinsaf.core.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
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