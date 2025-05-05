package com.example.uzi.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.uzi.ui.screens.AppNavigation
import com.mrinsaf.auth.ui.viewModel.authorisation.AuthorisationViewModel
import com.mrinsaf.auth.ui.viewModel.registraion.RegistraionViewModel
import com.mrinsaf.core.ui.theme.UziTheme
import com.mrinsaf.diagnostic_details.ui.viewModel.DiagnosticViewModel
import com.mrinsaf.diagnostic_list.ui.viewModel.DiagnosticListViewModel
import com.mrinsaf.newdiagnostic.ui.viewModel.NewDiagnosticViewModel
import com.mrinsaf.profile.ui.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authorisationViewModel: AuthorisationViewModel by viewModels()
        println(authorisationViewModel)

        enableEdgeToEdge()

        val registrationViewModel: RegistraionViewModel by viewModels()
        val newDiagnosticViewModel: NewDiagnosticViewModel by viewModels()
        val diagnosticViewModel: DiagnosticViewModel by viewModels()
        val diagnosticListViewModel: DiagnosticListViewModel by viewModels()
        val profileViewModel: ProfileViewModel by viewModels()

        setContent {
            UziTheme(dynamicColor = false) {
                val navController = rememberNavController()
                val authorisationUiState by authorisationViewModel.uiState.collectAsStateWithLifecycle()

                AppNavigation(
                    navController = navController,
                    startDestination = AuthScreens.SplashScreen.route,
                    authorisationViewModel = authorisationViewModel,
                    registrationViewModel = registrationViewModel,
                    newDiagnosticViewModel = newDiagnosticViewModel,
                    diagnosticViewModel = diagnosticViewModel,
                    diagnosticListViewModel = diagnosticListViewModel,
                    patientId = authorisationUiState.patientId ?: "Unknown patient",
                    profileViewModel = profileViewModel,
                )
            }
        }
    }
}

