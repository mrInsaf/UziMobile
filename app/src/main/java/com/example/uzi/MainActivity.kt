package com.example.uzi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                val navController = rememberNavController()
                val authorisationUiState = authorisationViewModel.uiState.collectAsState().value

                NavHost(
                    navController = navController,
                    startDestination = AuthScreens.AuthorisationScreenRoute.route,
                ) {
                    composable(route = AuthScreens.AuthorisationScreenRoute.route) {
                        AuthorizationScreen(
                            authorisationViewModel = authorisationViewModel,
                            onSubmitLoginButtonClick = { authorisationViewModel.onSubmitLogin() },
                            onRegistrationButtonClick = { navController.navigate(
                                AuthScreens.RegistrationScreenRoute.route
                            ) },
                        )
                    }
                    composable(route = AuthScreens.RegistrationScreenRoute.route) {
                        RegistrationScreen(
                            registrationViewModel = registrationViewModel
                        )
                    }
                    composable(route = AuthScreens.MainScreenRoute.route) {
                        MainScreen(
                            newDiagnosticViewModel = newDiagnosticViewModel,
                            userData = authorisationUiState.userData
                        )
                    }
                }


                LaunchedEffect(authorisationUiState.isAuthorised) {
                    if (authorisationUiState.isAuthorised) {
                        navController.navigate(AuthScreens.MainScreenRoute.route) {
                            popUpTo(0)
                        }
                    }
                    else {
                        navController.navigate(AuthScreens.AuthorisationScreenRoute.route)
                    }
                }

            }
        }
    }
    sealed class AuthScreens(val route: String) {
        object AuthorisationScreenRoute : AuthScreens("authorise")
        object RegistrationScreenRoute : AuthScreens("register")
        object MainScreenRoute : AuthScreens("main")
    }
}

