package com.example.uzi.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.uzi.ui.AuthScreens
import com.mrinsaf.auth.ui.screens.AuthorizationScreen
import com.mrinsaf.auth.ui.screens.RegistrationScreen
import com.mrinsaf.auth.ui.viewModel.authorisation.AuthorisationUiState
import com.mrinsaf.auth.ui.viewModel.authorisation.AuthorisationViewModel
import com.mrinsaf.auth.ui.viewModel.registraion.RegistraionViewModel
import com.mrinsaf.core.data.network.AuthEventBus
import com.mrinsaf.diagnostic_details.ui.viewModel.DiagnosticViewModel
import com.mrinsaf.diagnostic_list.ui.viewModel.DiagnosticListViewModel
import com.mrinsaf.newdiagnostic.ui.viewModel.NewDiagnosticViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    val context = LocalContext.current

    val authState = authorisationViewModel.authState.collectAsStateWithLifecycle()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        composable(route = AuthScreens.AuthorisationScreen.route) {
            AuthorizationScreen(
                authorisationViewModel = authorisationViewModel,
                onSubmitLoginButtonClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        authorisationViewModel.onSubmitLogin()
                    }
                },
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

    LaunchedEffect(authState.value) {
        when(authState.value) {
            AuthorisationViewModel.AuthState.Authorized -> navController.navigate(AuthScreens.MainScreen.route)
            AuthorisationViewModel.AuthState.Unauthorized -> {
                Toast.makeText(
                    context,
                    "Сессия истекла...",
                    Toast.LENGTH_SHORT
                ).show()
                println("перехожу на экран авторизации")
                navController.navigate(AuthScreens.AuthorisationScreen.route)
            }
            AuthorisationViewModel.AuthState.Loading -> Unit
        }
    }

    LaunchedEffect(Unit) {
        AuthEventBus.authRequired.collect {
            println("Получено событие: требуется авторизация")
            authorisationViewModel.onTokenExpired()
        }
    }
}

