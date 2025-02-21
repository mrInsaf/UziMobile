package com.example.uzi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.mrinsaf.core.data.repository.local.TokenStorage
import com.mrinsaf.core.ui.theme.UziTheme
import com.mrinsaf.auth.ui.viewModel.authorisation.AuthorisationViewModel
import com.mrinsaf.core.ui.viewModel.diagnostic.DiagnosticViewModel
import com.mrinsaf.core.ui.viewModel.diagnosticList.DiagnosticListViewModel
import com.mrinsaf.newdiagnostic.ui.viewModel.NewDiagnosticViewModel
import com.mrinsaf.auth.ui.viewModel.registraion.RegistraionViewModel
import com.mrinsaf.core.data.repository.local.UserInfoStorage
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

        val context = this

        val authorisationViewModel: com.mrinsaf.auth.ui.viewModel.authorisation.AuthorisationViewModel by viewModels()
        val registrationViewModel: com.mrinsaf.auth.ui.viewModel.registraion.RegistraionViewModel by viewModels()
        val newDiagnosticViewModel: NewDiagnosticViewModel by viewModels()
        val diagnosticViewModel: DiagnosticViewModel by viewModels()
        val diagnosticListViewModel: DiagnosticListViewModel by viewModels()

        var patientId: String? = null

        lifecycleScope.launch {
            println("Достаю userid")
            patientId = UserInfoStorage.getUserId(context = context).firstOrNull() ?: ""
            println("patientId: $patientId")

            if (patientId?.isBlank() == true) {
                println("Patient ID is empty")
                UserInfoStorage.saveUserId(context = context, userId = "72881f74-1d10-4d93-9002-5207a83729ed")
                // TODO поменять на получение настоящего id
            } else {
                println("Patient ID: $patientId")
            }
        } // TODO Переместить в отдельную функцию

        retrieveTokens()

        authorisationViewModel.observeTokenExpiration(newDiagnosticViewModel.uiEvent)
        newDiagnosticViewModel.uiEvent
            .onEach {
                Toast.makeText(this, "Сессия истекла", Toast.LENGTH_SHORT).show()
            }
            .launchIn(lifecycleScope)

        setContent {
            UziTheme(dynamicColor = false) {
                val navController = rememberNavController()
                val authorisationUiState by authorisationViewModel.uiState.collectAsState()

                AppNavigation(
                    navController = navController,
                    authorisationUiState = authorisationUiState,
                    authorisationViewModel = authorisationViewModel,
                    registrationViewModel = registrationViewModel,
                    newDiagnosticViewModel = newDiagnosticViewModel,
                    diagnosticViewModel = diagnosticViewModel,
                    diagnosticListViewModel = diagnosticListViewModel,
                    patientId = patientId ?: "",
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

