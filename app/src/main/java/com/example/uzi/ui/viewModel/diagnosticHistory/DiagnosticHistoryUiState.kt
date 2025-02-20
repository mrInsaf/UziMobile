package com.example.uzi.ui.viewModel.diagnosticHistory

import android.net.Uri
import com.example.uzi.data.models.networkResponses.NodesSegmentsResponse
import com.example.uzi.data.models.basic.Uzi
import com.example.uzi.data.models.basic.UziImage


data class DiagnosticHistoryUiState(
    var uziList: List<Uzi> = emptyList(),
    var currentUziId: String = "",

    var completedDiagnosticId: String = "",

    var downloadedImagesUris: MutableList<Uri> = mutableListOf(),

    var uziImages: List<UziImage> = emptyList(),

    var nodesAndSegmentsResponses: List<NodesSegmentsResponse> = emptyList(),

    var selectedDiagnosticId: String = "",
    var selectedDiagnosticDate: String = "",
    var selectedClinicName: String? = null,

    var isRecommendationSheetVisible: Boolean = false,

    )
