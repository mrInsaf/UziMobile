package com.mrinsaf.newdiagnostic.ui.viewModel

import android.graphics.Bitmap
import android.net.Uri
import com.mrinsaf.core.data.models.networkResponses.NodesSegmentsResponse
import com.mrinsaf.core.data.models.basic.Uzi
import com.mrinsaf.core.data.models.basic.UziImage

data class NewDiagnosticUiState(
    var currentScreenIndex: Int = 0,

    var dateOfAdmission: String = "",
    var clinicName: String = "",

    var saveResultsChecked: Boolean = false,

    var selectedImageUri: Uri? = null,

    var diagnosticProcessState: DiagnosticProcessState = DiagnosticProcessState.Idle,

    val uziImagesBmp: Map<String, Bitmap> = emptyMap(),

//    var isDiagnosticSent: Boolean = false,

    var completedDiagnosticId: String = "",

    var completedDiagnosticInformation: Uzi? = null,

    var downloadedImagesUri: Uri? = null,

    var uziImages: List<UziImage> = emptyList(),

    var nodesAndSegmentsResponses: List<NodesSegmentsResponse> = emptyList(),

//    var isDiagnosticFailed: Boolean = false,
)

sealed class DiagnosticProcessState  {
    data object Idle : DiagnosticProcessState() // начальное состояние
    data object Sending : DiagnosticProcessState() // диагностика отправляется
    data class Success(val diagnosticId: String) : DiagnosticProcessState() // успешно завершено
    data object Failure : DiagnosticProcessState() // произошла ошибка
}

val DiagnosticProcessState.isSuccess: Boolean
    get() = this is DiagnosticProcessState.Success