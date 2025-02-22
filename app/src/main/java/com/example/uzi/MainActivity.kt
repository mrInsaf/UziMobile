package com.example.uzi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.mrinsaf.auth.ui.viewModel.authorisation.AuthorisationUiState
import com.mrinsaf.auth.ui.viewModel.authorisation.AuthorisationViewModel
import com.mrinsaf.auth.ui.viewModel.registraion.RegistraionViewModel
import com.mrinsaf.core.data.repository.local.TokenStorage
import com.mrinsaf.core.ui.theme.UziTheme
import com.mrinsaf.diagnostic_list.ui.viewModel.DiagnosticListViewModel
import com.mrinsaf.newdiagnostic.ui.viewModel.NewDiagnosticViewModel
import com.mrinsaf.core.data.repository.local.UserInfoStorage
import com.mrinsaf.diagnostic_details.ui.viewModel.DiagnosticViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val authorisationViewModel: AuthorisationViewModel by viewModels()
        val registrationViewModel: RegistraionViewModel by viewModels()
        val newDiagnosticViewModel: NewDiagnosticViewModel by viewModels()
        val diagnosticViewModel: DiagnosticViewModel by viewModels()
        val diagnosticListViewModel: DiagnosticListViewModel by viewModels()

        retrieveTokens()

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

    private fun retrieveTokens() {
        lifecycleScope.launch {
            try {
                val accessToken = TokenStorage.getAccessToken(this@MainActivity).first()
                val refreshToken = TokenStorage.getRefreshToken(this@MainActivity).first()
                println("accessToken: $accessToken")
                println("refreshToken: $refreshToken")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

