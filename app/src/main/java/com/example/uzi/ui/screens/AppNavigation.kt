package com.example.uzi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.uzi.ui.AuthScreens
import com.mrinsaf.auth.ui.screens.AuthorizationScreen
import com.mrinsaf.auth.ui.screens.RegistrationScreen
import com.mrinsaf.auth.ui.viewModel.authorisation.AuthorisationUiState
import com.mrinsaf.auth.ui.viewModel.authorisation.AuthorisationViewModel
import com.mrinsaf.diagnostic_details.ui.viewModel.DiagnosticViewModel
import com.mrinsaf.diagnostic_list.ui.viewModel.DiagnosticListViewModel
import com.mrinsaf.newdiagnostic.ui.viewModel.NewDiagnosticViewModel
import com.mrinsaf.auth.ui.viewModel.registraion.RegistraionViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String,
    authorisationUiState: AuthorisationUiState,
    authorisationViewModel: AuthorisationViewModel,
    registrationViewModel: RegistraionViewModel,
    newDiagnosticViewModel: NewDiagnosticViewModel,
    diagnosticViewModel: DiagnosticViewModel,
    diagnosticListViewModel: DiagnosticListViewModel,
    patientId: String,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        composable(route = AuthScreens.AuthorisationScreen.route) {
            AuthorizationScreen(
                authorisationViewModel = authorisationViewModel,
                onSubmitLoginButtonClick = { authorisationViewModel.onSubmitLogin() },
                onRegistrationButtonClick = {
                    navController.navigate(AuthScreens.RegistrationScreen.route)
                },
            )
        }
        composable(route = AuthScreens.RegistrationScreen.route) {
            RegistrationScreen(
                registrationViewModel = registrationViewModel
            )
        }
        composable(route = AuthScreens.MainScreen.route) {
            MainScreen(
                newDiagnosticViewModel = newDiagnosticViewModel,
                userData = authorisationUiState.userData,
                diagnosticViewModel = diagnosticViewModel,
                diagnosticListViewModel = diagnosticListViewModel,
                patientId = patientId
            )
        }
        composable(route = AuthScreens.SplashScreen.route) { Unit }
    }

}

