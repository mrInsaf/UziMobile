package com.example.uzi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.uzi.ui.screens.AuthorizationScreen
import com.example.uzi.ui.theme.UziTheme
import com.example.uzi.ui.viewModel.authorisation.AuthorisationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authorisationViewModel = AuthorisationViewModel()

        enableEdgeToEdge()
        setContent {
            UziTheme {
                AuthorizationScreen(authorisationViewModel)
            }
        }
    }
}