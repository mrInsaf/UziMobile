package com.mrinsaf.auth.ui.viewModel.registraion

data class RegistrationUiState(
    val surname: String = "",
    val name: String = "",
    val patronymic: String = "",
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val dateOfBirth: String = "",
    val policy: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val repeatPasswordError: String? = null,
    val blankFieldsError: String? = null
)
