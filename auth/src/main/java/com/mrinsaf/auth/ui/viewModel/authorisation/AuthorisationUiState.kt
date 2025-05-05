package com.mrinsaf.auth.ui.viewModel.authorisation

data class AuthorisationUiState(
    var authorizationEmail: String = "",
    var authorizationPassword: String = "",

    var patientId: String? = "",
)
