package com.mrinsaf.auth.ui.viewModel.registraion

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrinsaf.core.data.models.networkRequests.RegPatientRequest
import com.mrinsaf.core.data.repository.network.UziServiceRepository
import com.mrinsaf.core.data.repository.local.UserInfoStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RegistraionViewModel @Inject constructor(
    val repository: UziServiceRepository,
    @ApplicationContext val context: Context,
) : ViewModel() {
    private var _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState>
        get() = _uiState

    private val _registrationSuccess = MutableSharedFlow<Unit>()
    val registrationSuccess: SharedFlow<Unit> = _registrationSuccess

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

    fun registerPatient() = viewModelScope.launch {
        val patientData = RegPatientRequest(
            fullname = "${uiState.value.surname} ${uiState.value.name} ${uiState.value.patronymic}".trim(),
            policy = uiState.value.policy,
            birthDate = convertDate(uiState.value.dateOfBirth)!!,
            email = uiState.value.email,
            password = uiState.value.password
        )
        try {
            val patientId = repository.regPatient(patientData)
            println("patientId: $patientId")

            UserInfoStorage.saveUserId(context, patientId.id)
            _registrationSuccess.emit(Unit)

        } catch (e: Exception) {
            println(e)
            Toast.makeText(
                context,
                "Что-то пошло не так",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun convertDate(inputDate: String): String? {
        val inputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return try {
            val parsedDate = inputFormat.parse(inputDate)
            outputFormat.format(parsedDate!!)
        } catch (e: Exception) {
            println("Ошибка при парсинге даты рождения: $e")
            null
        }
    }

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