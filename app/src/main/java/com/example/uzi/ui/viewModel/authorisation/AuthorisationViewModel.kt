package com.example.uzi.ui.viewModel.authorisation

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uzi.ui.UiEvent
import com.mrinsaf.core.data.repository.UziServiceRepository
import com.mrinsaf.core.data.repository.local.TokenStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
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

    init {
        viewModelScope.launch {
            // Проверка наличия токенов в DataStore
            val accessToken = TokenStorage.getAccessToken(context).firstOrNull()
            val refreshToken = TokenStorage.getRefreshToken(context).firstOrNull()

            // Если оба токена существуют, считаем, что пользователь авторизован
            _uiState.update { 
                it.copy(isAuthorised = !accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty())
            }
            
        }
    }

    private fun onTokenExpired() {
        _uiState.update { it.copy(isAuthorised = false) }
    }

    // Метод для подписки на события истечения токена
    fun observeTokenExpiration(tokenExpiredEvent: SharedFlow<UiEvent>) {
        viewModelScope.launch {
            println("Начинаю наблюдение за tokenExpiredEvent")
            tokenExpiredEvent.collect {
                println("Событие получено: $it")
                try {
                    onTokenExpired()
                } catch (e: Exception) {
                    println("Ошибка в onTokenExpired: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }


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
                    println("yo")
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
//                val userData = async {
//                    repository.getUser()
//                }.await()

                _uiState.update {
                    it.copy(
                        isAuthorised = true,
//                        userData = userData
                    )
                }
            } else {
                Toast.makeText(context, "Неверные почта или пароль", Toast.LENGTH_LONG).show()
            }
        }
    }


}