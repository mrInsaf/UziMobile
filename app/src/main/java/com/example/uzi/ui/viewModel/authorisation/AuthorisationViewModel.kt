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
                try {
                    println("email: ${uiState.value.authorizationEmail}")
                    println("pwd: ${uiState.value.authorizationPassword}")
                    repository.submitLogin(
                        email = uiState.value.authorizationEmail,
                        password = uiState.value.authorizationPassword
                    )
                } catch (e: Exception) {
                    if (e is retrofit2.HttpException) {
                        val response = e.response() // Получаем ответ с ошибкой
                        val errorBody = response?.errorBody()?.string() // Тело ошибки в виде строки
                        println("Ошибка HTTP: ${e.code()} - ${e.message()}")
                        println("Тело ошибки: $errorBody")
                    } else {
                        println("Ошибка при попытке логинa: $e")
                    }
                    null
                }
            }

            val loginResponse = isAuthorisedResponse.await()
            println("loginResponse: $loginResponse")

            if (loginResponse != null) {
                val userData = async {
                    repository.getUser()
                }.await()

                _uiState.update {
                    it.copy(
                        isAuthorised = true, // Обновляем состояние с успешной авторизацией
                        userData = userData
                    )
                }
            } else { // Если loginResponse == null, авторизация не удалась
                Toast.makeText(context, "Неверные почта или пароль", Toast.LENGTH_LONG).show()
            }
        }
    }


}