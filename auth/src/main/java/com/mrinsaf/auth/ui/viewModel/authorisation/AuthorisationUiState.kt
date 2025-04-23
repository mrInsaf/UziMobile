package com.mrinsaf.auth.ui.viewModel.authorisation

import com.mrinsaf.core.data.models.User

data class AuthorisationUiState(
    var authorizationEmail: String = "",
    var authorizationPassword: String = "",

    var patientId: String? = "",

    var userData: User = User()
)
