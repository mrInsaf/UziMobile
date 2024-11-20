package com.example.uzi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import com.example.uzi.data.repository.MockUziServiceRepository
import com.example.uzi.data.repository.UziServiceRepository
import com.example.uzi.ui.components.DateFormField
import com.example.uzi.ui.screens.AuthorizationScreen
import com.example.uzi.ui.screens.MainScreen
import com.example.uzi.ui.screens.RegistrationScreen
import com.example.uzi.ui.theme.UziTheme
import com.example.uzi.ui.viewModel.authorisation.AuthorisationViewModel
import com.example.uzi.ui.viewModel.newDiagnostic.NewDiagnosticViewModel
import com.example.uzi.ui.viewModel.registraion.RegistraionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val context = this

        val repository: UziServiceRepository = MockUziServiceRepository()
        val authorisationViewModel = AuthorisationViewModel(
            repository,
            context = context
        )
        val registrationViewModel = RegistraionViewModel()
        val newDiagnosticViewModel = NewDiagnosticViewModel()

        enableEdgeToEdge()
        setContent {
            UziTheme {
                val authorisationUiState = authorisationViewModel.uiState.collectAsState().value

                if (authorisationUiState.isAuthorised) {
                    MainScreen(
                        newDiagnosticViewModel = newDiagnosticViewModel
                    )
                }
                else {
                    AuthorizationScreen(
                        authorisationViewModel = authorisationViewModel,
                        onSubmitLoginButtonClick = { authorisationViewModel.onSubmitLogin() }
                    )
                }

//                RegistrationScreen(registrationViewModel)
            }
        }
    }
}