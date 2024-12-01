package com.example.uzi.ui.viewModel.diagnosticHistory

import com.example.uzi.data.models.ReportResponse


data class DiagnosticHistoryUiState(
    var uziIds: MutableList<String> = mutableListOf(),
    var currentUziId: String = "",

    var currentResponse: ReportResponse = ReportResponse(
        formations = null,
        images = null,
        segments = null,
        uzi = null
    )
)
