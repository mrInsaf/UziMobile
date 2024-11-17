package com.example.uzi.ui.screens.newDiagnosticScreens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uzi.ui.components.CircleNumber
import com.example.uzi.ui.components.NewDiagnosticProgressBar

@Composable
fun NewDiagnosticNavigation() {
    val navController = rememberNavController()
    Scaffold(

    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
//            Spacer(modifier = Modifier.size(20.dp))
            Column (
                modifier = Modifier.fillMaxWidth(0.85f)
            ){
                TextButton(
                    onClick = { },
                ) {
                    Text(
                        text = "Назад",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                NewDiagnosticProgressBar(currentScreenIndex = 0)

                NavHost(
                    navController = navController,
                    startDestination = NewDiagnosticScreen.ImageLoad.route
                ) {
                    composable(NewDiagnosticScreen.ImageLoad.route) {
                        UploadImage(
                            onStartDiagnosticClick = {
                                TODO()
                            },
                            modifier = Modifier.padding(padding)
                        )
                    }

                    composable(NewDiagnosticScreen.AdditionalInformation.route) {
                        AdditionalInformation(modifier = Modifier.padding(padding))
                    }

                    composable(NewDiagnosticScreen.DiagnosticLoading.route) {
                        DiagnosticLoading(modifier = Modifier.padding(padding))
                    }
                }
            }
        }
    }

}

sealed class NewDiagnosticScreen(val route: String) {
    object ImageLoad : NewDiagnosticScreen("image_load")
    object AdditionalInformation : NewDiagnosticScreen("additional_information")
    object DiagnosticLoading : NewDiagnosticScreen("diagnostic_loading")
}

@Preview
@Composable
fun NewDiagnosticNavigationPreview() {
    NewDiagnosticNavigation()
}