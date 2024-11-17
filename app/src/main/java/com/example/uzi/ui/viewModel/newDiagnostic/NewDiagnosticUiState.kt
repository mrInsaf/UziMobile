package com.example.uzi.ui.viewModel.newDiagnostic

data class NewDiagnosticUiState(
    var currentScreenIndex: Int = 0,

    var dateOfAdmission: String = "",
    var clinicName: String = "",

    var saveResultsChecked: Boolean = false,

    )
