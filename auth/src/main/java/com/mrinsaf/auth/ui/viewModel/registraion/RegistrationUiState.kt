package com.mrinsaf.auth.ui.viewModel.registraion

import java.security.Policy

data class RegistrationUiState(
    val surname: String = "",
    val name: String = "",
    val patronymic: String = "",
    val email: String = "",
    val dateOfBirth: String = "",
    val policy: String = "",
    val password: String = "",
    val repeatPassword: String = "",


)
