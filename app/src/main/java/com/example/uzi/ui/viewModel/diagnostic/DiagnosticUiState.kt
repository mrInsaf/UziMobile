package com.example.uzi.ui.viewModel.diagnostic

import android.net.Uri
import com.example.uzi.data.models.networkResponses.NodesSegmentsResponse
import com.example.uzi.data.models.basic.Uzi
import com.example.uzi.data.models.basic.UziImage


data class DiagnosticUiState(

    var currentUziId: String = "",

    var completedDiagnosticId: String = "",
    var completedUziNodesAndSegments: List<NodesSegmentsResponse> = emptyList(),

    var downloadedImagesUris: MutableList<Uri> = mutableListOf(),

    var uziImages: List<UziImage> = emptyList(),

    var selectedDiagnosticId: String = "",
    var selectedDiagnosticDate: String = "",
    var selectedClinicName: String? = null,

    var isRecommendationSheetVisible: Boolean = false,

    )
