package com.example.uzi.ui.viewModel.authorisation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


class AuthorisationViewModel : ViewModel() {
    private var _uiState = MutableStateFlow(AuthorisationUiState())
    val uiState: StateFlow<AuthorisationUiState>
        get() = _uiState

    fun onAuthorizationEmailChange(newEmail: String) {
        _uiState.update { it.copy(authorizationEmail = newEmail) }
    }

    fun onAuthorizationPasswordChange(newPassword: String) {
        _uiState.update { it.copy(authorizationPassword = newPassword) }
    }




}