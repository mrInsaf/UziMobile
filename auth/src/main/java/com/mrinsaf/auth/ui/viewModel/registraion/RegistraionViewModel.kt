package com.mrinsaf.auth.ui.viewModel.registraion

import android.util.Patterns
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.io.path.Path

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

    fun onDatePick(selectedDate: String) {
        _uiState.update { it.copy(dateOfBirth = selectedDate) }
    }

    fun onPolicyChange(newPolicy: String) {
        _uiState.update { it.copy(policy = newPolicy) }
    }

    // Основной метод валидации
    fun validateForm(): Boolean {
        val currentState = _uiState.value
        val emailError = validateEmail(currentState.email)
        val passwordError = validatePassword(currentState.password)
        val repeatPassError = validateRepeatPassword(currentState.password, currentState.repeatPassword)
        val blankFieldsError = validateBlankFields()

        _uiState.update {
            it.copy(
                emailError = emailError,
                passwordError = passwordError,
                repeatPasswordError = repeatPassError,
                blankFieldsError = blankFieldsError,
            )
        }

        return emailError == null &&
                passwordError == null &&
                repeatPassError == null &&
                blankFieldsError == null
    }

    // Отдельные методы валидации
    private fun validateEmail(email: String): String? {
        return if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Некорректный email"
        else null
    }

    private fun validateBlankFields(): String? {
        val currentState = _uiState.value
        if (
            currentState.password.isBlank() ||
            currentState.email.isBlank() ||
            currentState.surname.isBlank() ||
            currentState.name.isBlank() ||
            currentState.dateOfBirth.isBlank() ||
            currentState.policy.isBlank()
        ) return "Все поля должны быть заполнены"
        else return null
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.length < 8 -> "Пароль. Минимум 8 символов"
            !password.any { it.isDigit() } -> "Пароль. Должен содержать цифры"
            !password.any { it.isLetter() } -> "Пароль. Должен содержать буквы"
            else -> null
        }
    }

    private fun validateRepeatPassword(password: String, repeatPassword: String): String? {
        return if (repeatPassword != password) "Пароли не совпадают" else null
    }

}