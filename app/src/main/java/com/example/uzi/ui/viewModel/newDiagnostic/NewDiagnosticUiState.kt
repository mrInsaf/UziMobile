package com.example.uzi.ui.viewModel.newDiagnostic

import android.net.Uri
import com.example.uzi.data.models.networkResponses.NodesSegmentsResponse
import com.example.uzi.data.models.networkResponses.UziImage

data class NewDiagnosticUiState(
    var currentScreenIndex: Int = 0,

    var dateOfAdmission: String = "",
    var clinicName: String = "",

    var saveResultsChecked: Boolean = false,

    var selectedImageUris: List<Uri> = emptyList(),

    var isDiagnosticSent: Boolean = false,

    var completedDiagnosticId: String = "",

    var downloadedImagesUris: MutableList<Uri> = mutableListOf(),

    var uziImages: List<UziImage> = emptyList(),

    var nodesAndSegmentsResponse: NodesSegmentsResponse = NodesSegmentsResponse(emptyList(), emptyList())
)
