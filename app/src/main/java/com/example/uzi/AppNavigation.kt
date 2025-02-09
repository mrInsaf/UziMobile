package com.example.uzi

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.uzi.ui.screens.AuthorizationScreen
import com.example.uzi.ui.screens.MainScreen
import com.example.uzi.ui.screens.RegistrationScreen
import com.example.uzi.ui.viewModel.authorisation.AuthorisationUiState
import com.example.uzi.ui.viewModel.authorisation.AuthorisationViewModel
import com.example.uzi.ui.viewModel.diagnosticHistory.DiagnosticHistoryViewModel
import com.example.uzi.ui.viewModel.newDiagnostic.NewDiagnosticViewModel
import com.example.uzi.ui.viewModel.registraion.RegistraionViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    authorisationUiState: AuthorisationUiState, // Предполагаемый тип состояния авторизации
    authorisationViewModel: AuthorisationViewModel,
    registrationViewModel: RegistraionViewModel,
    newDiagnosticViewModel: NewDiagnosticViewModel,
    diagnosticHistoryViewModel: DiagnosticHistoryViewModel,
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
                diagnosticHistoryViewModel = diagnosticHistoryViewModel,
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
