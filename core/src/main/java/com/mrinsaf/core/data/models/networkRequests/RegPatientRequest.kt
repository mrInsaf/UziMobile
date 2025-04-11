package com.mrinsaf.core.data.models.networkRequests
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegPatientRequest(
    @SerialName("fullname") val fullname: String,
    @SerialName("policy") val policy: String,
    @SerialName("birth_date") val birthDate: String,
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)
