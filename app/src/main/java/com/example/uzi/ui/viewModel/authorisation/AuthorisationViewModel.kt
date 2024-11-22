package com.example.uzi.ui.viewModel.authorisation

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uzi.data.repository.UziServiceRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AuthorisationViewModel(
    val repository: UziServiceRepository,
    val context: Context,
) : ViewModel() {
    private var _uiState = MutableStateFlow(AuthorisationUiState())
    val uiState: StateFlow<AuthorisationUiState>
        get() = _uiState

    fun onAuthorizationEmailChange(newEmail: String) {
        _uiState.update { it.copy(authorizationEmail = newEmail) }
    }

    fun onAuthorizationPasswordChange(newPassword: String) {
        _uiState.update { it.copy(authorizationPassword = newPassword) }
    }

    fun onSubmitLogin() {
        viewModelScope.launch {
            val isAuthorisedResponse = async {
                repository.submitLogin(
                    email = uiState.value.authorizationEmail,
                    password = uiState.value.authorizationPassword
                )
            }
            val isAuthorised = isAuthorisedResponse.await()
            if (isAuthorised) {

                val userData = async {
                    repository.getUser()
                }.await() // TODO(Это скорее всего будет по другому)

                _uiState.update { it.copy(
                    isAuthorised = isAuthorised,
                    userData = userData
                ) }
            }
            else {
                Toast.makeText(context, "Неверные почта или пароль", Toast.LENGTH_LONG).show()
            }
        }
    }


}