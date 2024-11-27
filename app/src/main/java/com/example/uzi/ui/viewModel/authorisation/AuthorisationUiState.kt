package com.example.uzi.ui.viewModel.authorisation

import com.example.uzi.data.models.User

data class AuthorisationUiState(
    var authorizationEmail: String = "",
    var authorizationPassword: String = "",

    var isAuthorised: Boolean = true,

    var userData: User = User()
)
