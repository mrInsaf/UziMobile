package com.mrinsaf.core.data.network.dto.network_request
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
