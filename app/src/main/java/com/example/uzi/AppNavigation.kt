package com.example.uzi

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mrinsaf.auth.ui.screens.AuthorizationScreen
import com.example.uzi.ui.MainScreen
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
    authorisationUiState: AuthorisationUiState,
    authorisationViewModel: AuthorisationViewModel,
    registrationViewModel: RegistraionViewModel,
    newDiagnosticViewModel: NewDiagnosticViewModel,
    diagnosticViewModel: DiagnosticViewModel,
    diagnosticListViewModel: com.mrinsaf.diagnostic_list.ui.viewModel.DiagnosticListViewModel,
    patientId: String,
) {
    NavHost(
        navController = navController,
        startDestination = AuthScreens.AuthorisationScreenRoute.route,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        composable(route = AuthScreens.AuthorisationScreenRoute.route) {
            AuthorizationScreen(
                authorisationViewModel = authorisationViewModel,
                onSubmitLoginButtonClick = { authorisationViewModel.onSubmitLogin() },
                onRegistrationButtonClick = {
                    navController.navigate(AuthScreens.RegistrationScreenRoute.route)
                },
            )
        }
        composable(route = AuthScreens.RegistrationScreenRoute.route) {
            RegistrationScreen(
                registrationViewModel = registrationViewModel
            )
        }
        composable(route = AuthScreens.MainScreenRoute.route) {
            println("рисую главный экран")
            MainScreen(
                newDiagnosticViewModel = newDiagnosticViewModel,
                userData = authorisationUiState.userData,
                diagnosticViewModel = diagnosticViewModel,
                diagnosticListViewModel = diagnosticListViewModel,
                patientId = patientId
            )
        }
    }

    // Навигация в зависимости от состояния авторизации
    LaunchedEffect(authorisationUiState.isAuthorised) {
        println("Проверяю авторизацию")
        if (authorisationUiState.isAuthorised) {
            navController.navigate(AuthScreens.MainScreenRoute.route) {
                popUpTo(0)
            }
        } else {
            navController.navigate(AuthScreens.AuthorisationScreenRoute.route)
        }
    }
}

sealed class AuthScreens(val route: String) {
    object AuthorisationScreenRoute : AuthScreens("authorise")
    object RegistrationScreenRoute : AuthScreens("register")
    object MainScreenRoute : AuthScreens("main")
}
