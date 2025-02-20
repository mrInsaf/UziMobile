package com.example.uzi.ui.viewModel.authorisation

import com.mrinsaf.core.data.models.User

data class AuthorisationUiState(
    var authorizationEmail: String = "",
    var authorizationPassword: String = "",

    var isAuthorised: Boolean = false,

    var userData: com.mrinsaf.core.data.models.User = com.mrinsaf.core.data.models.User()
)
