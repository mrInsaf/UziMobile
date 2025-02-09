package com.example.uzi.ui.viewModel.diagnosticHistory

import android.net.Uri
import com.example.uzi.data.models.networkResponses.NodesSegmentsResponse
import com.example.uzi.data.models.networkResponses.ReportResponse
import com.example.uzi.data.models.networkResponses.Uzi
import com.example.uzi.data.models.networkResponses.UziImage


data class DiagnosticHistoryUiState(
    var uziList: List<Uzi> = emptyList(),
    var currentUziId: String = "",

    var currentResponse: ReportResponse = ReportResponse(
        formations = null,
        images = null,
        segments = null,
        fakeUzi = null
    ),

    var completedDiagnosticId: String = "",

    var downloadedImagesUris: MutableList<Uri> = mutableListOf(),

    var uziImages: List<UziImage> = emptyList(),

    var nodesAndSegmentsResponses: List<NodesSegmentsResponse> = emptyList(),

    var selectedDiagnosticId: String = "",
    var selectedDiagnosticDate: String = "",
    var selectedClinicName: String? = null,


)
