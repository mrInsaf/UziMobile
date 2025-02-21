package com.mrinsaf.core.ui.viewModel.diagnostic

import android.net.Uri
import com.mrinsaf.core.data.models.networkResponses.NodesSegmentsResponse
import com.mrinsaf.core.data.models.basic.UziImage


data class DiagnosticUiState(

    var currentUziId: String = "",

    var selectedUziNodesAndSegments: List<NodesSegmentsResponse> = emptyList(),

    var downloadedImagesUris: MutableList<Uri> = mutableListOf(),

    var uziImages: List<UziImage> = emptyList(),

    var selectedDiagnosticId: String = "",
    var selectedUziDate: String = "",
    var selectedClinicName: String? = null,

    var isRecommendationSheetVisible: Boolean = false,

    )
