package com.mrinsaf.auth.ui.viewModel.authorisation

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrinsaf.core.data.repository.network.UziServiceRepository
import com.mrinsaf.core.data.repository.local.UserInfoStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorisationViewModel @Inject constructor(
    val repository: UziServiceRepository,
    @ApplicationContext val context: Context,
) : ViewModel() {
    private var _uiState = MutableStateFlow(AuthorisationUiState())
    val uiState: StateFlow<AuthorisationUiState>
        get() = _uiState

    var authState = MutableStateFlow<AuthState>(AuthState.Loading)
        private set

    init {
        viewModelScope.launch {
            try {
                val isAuthorized = repository.getUziDevices().isNotEmpty()
                authState.value = if (isAuthorized) AuthState.Authorized else AuthState.Error.Unauthorized
            }
            catch (e: Exception) {
                when {
                    e.message?.contains("does not exist") == true -> authState.value = AuthState.Error.ApiIsDown
                    e.message?.contains("Failed to connect to") == true -> authState.value = AuthState.Error.ApiIsDown
                    e.message?.contains("timeout") == true -> authState.value = AuthState.Error.ApiIsDown
                    e.message?.contains("Read timed out") == true -> authState.value = AuthState.Error.ApiIsDown
                }
            }
        }
        updatePatientIdFromStorage()
    }

    fun onTokenExpired() {
        authState.value = AuthState.Error.Unauthorized
    }

    fun onAuthorizationEmailChange(newEmail: String) {
        _uiState.update { it.copy(authorizationEmail = newEmail) }
    }

    fun onAuthorizationPasswordChange(newPassword: String) {
        _uiState.update { it.copy(authorizationPassword = newPassword) }
    }

    suspend fun onSubmitLogin() {
        try {
            println("email: ${uiState.value.authorizationEmail}")
            println("pwd: ${uiState.value.authorizationPassword}")
            repository.submitLogin(
                email = uiState.value.authorizationEmail,
                password = uiState.value.authorizationPassword
            )
            authState.value = AuthState.Authorized
        } catch (e: Exception) {
            if (e is retrofit2.HttpException) {
                val response = e.response()
                val errorBody = response?.errorBody()?.string()
                println("Ошибка HTTP: ${e.code()} - ${e.message()}")
                println("Тело ошибки: $errorBody")
                if (errorBody != null && errorBody.contains("email: not found")) {
                    Toast.makeText(context, "Пользователь не найден", Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(context, "Неверные почта или пароль", Toast.LENGTH_LONG).show()
                }

            } else {
                println("Ошибка при попытке логинa: $e")
                Toast.makeText(context, "Произошла ошибка при авторизации", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updatePatientIdFromStorage() {
        viewModelScope.launch {
            println("updating patient id")
            val patientId = UserInfoStorage.saveDecodedUserIdFromToken(context)

            _uiState.update {
                it.copy(
                    patientId = patientId
                )
            }
        }
    }

    sealed class AuthState {
        object Loading: AuthState()
        object Authorized: AuthState()
        sealed class Error: AuthState() {
            object Unauthorized: Error()
            object ApiIsDown: Error()
        }
    }

}