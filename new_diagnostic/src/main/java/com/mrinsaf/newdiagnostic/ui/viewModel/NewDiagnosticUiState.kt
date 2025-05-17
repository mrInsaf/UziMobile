package com.mrinsaf.newdiagnostic.ui.viewModel

import android.graphics.Bitmap
import android.net.Uri
import com.mrinsaf.core.data.model.network_responses.NodesSegmentsResponse
import com.mrinsaf.core.domain.model.basic.Uzi
import com.mrinsaf.core.domain.model.basic.UziImage
import com.mrinsaf.core.presentation.ui.ui_state.DiagnosticProgressStateDetail

data class NewDiagnosticUiState(
    var currentScreenIndex: Int = 0,

    var dateOfAdmission: String = "",
    var clinicName: String = "",

    var saveResultsChecked: Boolean = false,

    var selectedImageUri: Uri? = null,
    var selectedImageName: String? = null,

    var isUziPosted: Boolean = false,

    var diagnosticProcessState: DiagnosticProcessState = DiagnosticProcessState.Idle,

    var diagnosticProgressStateDetail: DiagnosticProgressStateDetail = DiagnosticProgressStateDetail.UploadingUzi,
    var diagnosticProgressValue: Float = 0f,

    val uziImagesBmp: Map<String, Bitmap> = emptyMap(),

    var completedDiagnosticId: String = "",

    var completedDiagnosticInformation: Uzi? = null,

    var downloadedImagesUri: Uri? = null,

    var uziImages: List<UziImage> = emptyList(),

    var nodesAndSegmentsResponses: List<NodesSegmentsResponse> = emptyList(),
)

sealed class DiagnosticProcessState  {
    data object Idle : DiagnosticProcessState() // начальное состояние
    data object Sending : DiagnosticProcessState() // диагностика отправляется
    data class Success(val diagnosticId: String) : DiagnosticProcessState() // успешно завершено
    data object Failure : DiagnosticProcessState() // произошла ошибка
}

val DiagnosticProcessState.isSuccess: Boolean
    get() = this is DiagnosticProcessState.Success