package com.example.uzi.ui.viewModel.newDiagnostic

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uzi.data.models.ReportResponse
import com.example.uzi.data.repository.MockUziServiceRepository
import com.example.uzi.data.repository.UziServiceRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewDiagnosticViewModel(
    val repository: UziServiceRepository,
) : ViewModel() {
    private var _uiState = MutableStateFlow(NewDiagnosticUiState())
    val uiState: StateFlow<NewDiagnosticUiState>
        get() = _uiState

    fun onDatePick(newDate: String) {
        println("yooooo")
        _uiState.update { it.copy(dateOfAdmission = newDate) }
        println(uiState.value.dateOfAdmission)
    }

    fun onNextScreenButtonClick() {
        val currentScreenIndex = uiState.value.currentScreenIndex
        _uiState.update { it.copy(currentScreenIndex = currentScreenIndex + 1) }
        println(uiState.value.currentScreenIndex)
    }

    fun onPreviousButtonClick() {
        val currentScreenIndex = uiState.value.currentScreenIndex
        _uiState.update { it.copy(currentScreenIndex = currentScreenIndex - 1) }
    }

    fun onClinicNameInput(newClinicName: String) {
        _uiState.update { it.copy(clinicName = newClinicName) }
    }

    fun onSaveResultsCheck() {
        val saveResultsChecked = uiState.value.saveResultsChecked
        _uiState.update { it.copy(saveResultsChecked = !saveResultsChecked) }
    }

    fun onPhotoPickResult(uris: List<Uri>) {
        _uiState.update { it.copy(selectedImageUris = uris) }
        println("uiState.value.selectedImageUris: ${uiState.value.selectedImageUris}")
    }

    fun onDiagnosticStart(userId: String = "1") {
        viewModelScope.launch {
            val imageUris = uiState.value.selectedImageUris
            val dateOfAdmission = uiState.value.dateOfAdmission
            val clinicName = uiState.value.clinicName

            _uiState.update { it.copy(isDiagnosticSent = true) }

            val diagnosticId = repository.createUzi(
                patientId = userId,
                imageUris = imageUris,
                dateOfAdmission = dateOfAdmission,
                clinicName = clinicName,
            )
            _uiState.update { it.copy(completedDiagnosticId = diagnosticId) }
        }
    }
}