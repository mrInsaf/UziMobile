package com.example.uzi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uzi.ui.screens.DiagnosticScreensNavigation
import com.example.uzi.ui.screens.RegistrationScreen
import com.example.uzi.ui.screens.newDiagnosticScreens.NewDiagnosticNavigation
import com.example.uzi.ui.theme.UziTheme
import com.example.uzi.ui.viewModel.authorisation.AuthorisationViewModel
import com.example.uzi.ui.viewModel.registraion.RegistraionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authorisationViewModel = AuthorisationViewModel()
        val registrationViewModel = RegistraionViewModel()

        enableEdgeToEdge()
        setContent {
            UziTheme {
                RegistrationScreen()
            }
        }
    }
}