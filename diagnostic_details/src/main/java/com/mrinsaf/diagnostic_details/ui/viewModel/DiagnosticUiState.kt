package com.mrinsaf.diagnostic_details.ui.viewModel

import android.graphics.Bitmap
import android.net.Uri
import com.mrinsaf.core.data.models.networkResponses.NodesSegmentsResponse
import com.mrinsaf.core.data.models.basic.UziImage
import okhttp3.ResponseBody


data class DiagnosticUiState(

    var currentUziId: String = "",

    var selectedUziNodesAndSegments: List<NodesSegmentsResponse> = emptyList(),

    var downloadedImageUri: Uri? = null,

    var uziImages: List<UziImage> = emptyList(),
    var numberOfImages: Int = 0,
    val uziImagesBmp: Map<String, Bitmap> = emptyMap(),

    var selectedDiagnosticId: String = "",
    var selectedUziDate: String = "",
    var selectedClinicName: String? = null,

    var isRecommendationSheetVisible: Boolean = false,

    )
