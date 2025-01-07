package com.example.uzi.ui.viewModel.newDiagnostic

import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uzi.data.repository.UziServiceRepository
import com.example.uzi.ui.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class NewDiagnosticViewModel(
    val repository: UziServiceRepository,
) : ViewModel() {
    private var _uiState = MutableStateFlow(NewDiagnosticUiState())
    val uiState: StateFlow<NewDiagnosticUiState>
        get() = _uiState

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

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

            _uiState.update { it.copy(
                isDiagnosticSent = true,
                completedDiagnosticId = "",
            ) }

            try {
                val diagnosticId = repository.createUzi(
                    uziUris = imageUris,
                    projection = "long",
                    patientId = "72881f74-1d10-4d93-9002-5207a83729ed", // TODO переделать на авторизацию пользователя
                    deviceId = "1",
                )
                _uiState.update { it.copy(completedDiagnosticId = diagnosticId) }
            }
            catch (e: HttpException) {
                onTokenExpiration()
                _uiState.update {
                    it.copy(
                        currentScreenIndex = 0,
                        selectedImageUris = emptyList()
                    )
                }
                println("wtf $e")
            }
            catch (e: Exception) {

            }
        }
    }

    private fun onTokenExpiration() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.ShowToast("Сессия истекла"))
        }
    }
}