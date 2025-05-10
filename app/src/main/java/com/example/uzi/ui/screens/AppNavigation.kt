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
import com.mrinsaf.auth.ui.screens.errorScreens.ApiIsDownScreen
import com.mrinsaf.auth.ui.screens.splashScreen.SplashScreen
import com.mrinsaf.auth.ui.viewModel.authorisation.AuthorisationViewModel
import com.mrinsaf.auth.ui.viewModel.registraion.RegistraionViewModel
import com.mrinsaf.core.data.repository.auth.AuthEventBus
import com.mrinsaf.diagnostic_details.ui.viewModel.DiagnosticViewModel
import com.mrinsaf.diagnostic_list.ui.viewModel.DiagnosticListViewModel
import com.mrinsaf.newdiagnostic.ui.viewModel.NewDiagnosticViewModel
import com.mrinsaf.profile.ui.viewModel.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String,
    authorisationViewModel: AuthorisationViewModel,
    registrationViewModel: RegistraionViewModel,
    newDiagnosticViewModel: NewDiagnosticViewModel,
    diagnosticViewModel: DiagnosticViewModel,
    diagnosticListViewModel: DiagnosticListViewModel,
    profileViewModel: ProfileViewModel,
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
                diagnosticViewModel = diagnosticViewModel,
                diagnosticListViewModel = diagnosticListViewModel,
                patientId = patientId,
                profileViewModel = profileViewModel
            )
        }
        composable(route = AuthScreens.SplashScreen.route) {
            SplashScreen()
        }

        composable(route = AuthScreens.ApiIsDownScreen.route) {
            ApiIsDownScreen()
        }

    }

    LaunchedEffect(authState.value) {
        when (val state = authState.value) {
            is AuthorisationViewModel.AuthState.Authorized ->
                navController.navigate(AuthScreens.MainScreen.route)

            is AuthorisationViewModel.AuthState.Error -> {
                when (state) {
                    is AuthorisationViewModel.AuthState.Error.Unauthorized -> {
                        Toast.makeText(
                            context,
                            "Сессия истекла...",
                            Toast.LENGTH_SHORT
                        ).show()
                        println("перехожу на экран авторизации")
                        navController.navigate(AuthScreens.AuthorisationScreen.route)
                    }
                    is AuthorisationViewModel.AuthState.Error.ApiIsDown -> {
                        navController.navigate(AuthScreens.ApiIsDownScreen.route)
                    }
                }
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

    LaunchedEffect(Unit) {
        registrationViewModel.registrationSuccess.collect {
            Toast.makeText(
                context,
                "Успешная регистрация",
                Toast.LENGTH_SHORT
            ).show()

            navController.navigate(AuthScreens.AuthorisationScreen.route)
        }
    }
}

