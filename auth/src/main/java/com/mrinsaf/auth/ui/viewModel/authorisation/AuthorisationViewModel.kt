package com.mrinsaf.auth.ui.viewModel.authorisation

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.mrinsaf.core.data.repository.NetworkUziServiceRepository
import com.mrinsaf.core.data.repository.UziServiceRepository
import com.mrinsaf.core.data.repository.local.TokenStorage
import com.mrinsaf.core.data.repository.local.UserInfoStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        retrievePatientIdFromStorage()
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
                val response = e.response() // Получаем ответ с ошибкой
                val errorBody = response?.errorBody()?.string() // Тело ошибки в виде строки
                Toast.makeText(context, "Неверные почта или пароль", Toast.LENGTH_LONG).show()
                println("Ошибка HTTP: ${e.code()} - ${e.message()}")
                println("Тело ошибки: $errorBody")
            } else {
                println("Ошибка при попытке логинa: $e")
                Toast.makeText(context, "Произошла ошибка при авторизации", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun retrievePatientIdFromStorage() {
        viewModelScope.launch {
            val patientId = UserInfoStorage.getUserId(context = context).firstOrNull()

            if (patientId?.isBlank() == true) {
                println("Patient ID is empty")
                UserInfoStorage.saveUserId(context = context, userId = "72881f74-1d10-4d93-9002-5207a83729ed")
                // TODO поменять на получение настоящего id
            } else {
                println("Patient ID: $patientId")
            }
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