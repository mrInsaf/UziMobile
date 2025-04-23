package com.example.uzi.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.uzi.ui.screens.AppNavigation
import com.example.uzi.ui.viewModel.AppViewModel
import com.mrinsaf.auth.ui.viewModel.authorisation.AuthorisationViewModel
import com.mrinsaf.auth.ui.viewModel.registraion.RegistraionViewModel
import com.mrinsaf.core.data.repository.local.TokenStorage
import com.mrinsaf.core.ui.theme.UziTheme
import com.mrinsaf.diagnostic_details.ui.viewModel.DiagnosticViewModel
import com.mrinsaf.diagnostic_list.ui.viewModel.DiagnosticListViewModel
import com.mrinsaf.newdiagnostic.ui.viewModel.NewDiagnosticViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()

        super.onCreate(savedInstanceState)

        var isReady = false

        splash.setKeepOnScreenCondition { isReady }

        println(TokenStorage.accessToken.value)


        enableEdgeToEdge()
        val appViewModel: AppViewModel by viewModels()
        val authorisationViewModel: AuthorisationViewModel by viewModels()
        val registrationViewModel: RegistraionViewModel by viewModels()
        val newDiagnosticViewModel: NewDiagnosticViewModel by viewModels()
        val diagnosticViewModel: DiagnosticViewModel by viewModels()
        val diagnosticListViewModel: DiagnosticListViewModel by viewModels()

        println(appViewModel)
        setContent {

            UziTheme(dynamicColor = false) {
                val navController = rememberNavController()
                val authorisationUiState by authorisationViewModel.uiState.collectAsStateWithLifecycle()

                AppNavigation(
                    navController = navController,
                    authorisationUiState = authorisationUiState,
                    authorisationViewModel = authorisationViewModel,
                    registrationViewModel = registrationViewModel,
                    newDiagnosticViewModel = newDiagnosticViewModel,
                    diagnosticViewModel = diagnosticViewModel,
                    diagnosticListViewModel = diagnosticListViewModel,
                    patientId = authorisationUiState.patientId ?: "Unknown patient",
                )
            }
        }
    }
}

