package com.example.uzi.ui.viewModel.registraion

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegistraionViewModel @Inject constructor() : ViewModel() {
    private var _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState>
        get() = _uiState

    fun onSurnameChange(newSurname: String) {
        _uiState.update { it.copy(surname = newSurname) }
    }

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onPatronymicChange(newPatronymic: String) {
        _uiState.update { it.copy(patronymic = newPatronymic) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onRepeatPasswordChange(newRepeatPassword: String) {
        _uiState.update { it.copy(repeatPassword = newRepeatPassword) }
    }
}