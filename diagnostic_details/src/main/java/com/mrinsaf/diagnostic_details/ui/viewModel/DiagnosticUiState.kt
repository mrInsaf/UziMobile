package com.mrinsaf.diagnostic_details.ui.viewModel

import android.net.Uri
import com.mrinsaf.core.data.models.networkResponses.NodesSegmentsResponse
import com.mrinsaf.core.data.models.basic.UziImage


data class DiagnosticUiState(

    var currentUziId: String = "",

    var selectedUziNodesAndSegments: List<NodesSegmentsResponse> = emptyList(),

    var downloadedImageUri: Uri? = null,

    var uziImages: List<UziImage> = emptyList(),

    var selectedDiagnosticId: String = "",
    var selectedUziDate: String = "",
    var selectedClinicName: String? = null,

    var isRecommendationSheetVisible: Boolean = false,

    )
