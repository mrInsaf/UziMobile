package com.example.uzi.ui.viewModel.newDiagnostic

import android.net.Uri

data class NewDiagnosticUiState(
    var currentScreenIndex: Int = 0,

    var dateOfAdmission: String = "",
    var clinicName: String = "",

    var saveResultsChecked: Boolean = false,

    var selectedImageUris: List<Uri> = emptyList(),

    var isDiagnosticSent: Boolean = false,

    var completedDiagnosticId: String = "",
)
