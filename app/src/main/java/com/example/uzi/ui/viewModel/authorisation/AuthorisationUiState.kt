package com.example.uzi.ui.viewModel.authorisation

data class AuthorisationUiState(
    var authorizationEmail: String = "",
    var authorizationPassword: String = "",

    var isAuthorised: Boolean = false,

)
