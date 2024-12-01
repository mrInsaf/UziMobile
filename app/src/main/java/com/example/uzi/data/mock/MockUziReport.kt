package com.example.uzi.data.mock

import android.net.Uri

data class MockUziReport(
    val patientId: String,
    val imageUris: List<Uri>,
    val dateOfAdmission: String,
    val clinicName: String,
)
