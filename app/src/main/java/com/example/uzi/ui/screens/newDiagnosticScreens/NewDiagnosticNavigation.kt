package com.example.uzi.ui.screens.newDiagnosticScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uzi.ui.components.NewDiagnosticProgressBar
import com.example.uzi.ui.viewModel.newDiagnostic.NewDiagnosticViewModel

@Composable
fun NewDiagnosticNavigation(
    newDiagnosticViewModel: NewDiagnosticViewModel,
) {
    val newDiagnosticUiState = newDiagnosticViewModel.uiState.collectAsState().value
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
                    onClick = {
                        navController.popBackStack()
                        newDiagnosticViewModel.onPreviousButtonClick()
                    },
                    enabled = newDiagnosticUiState.currentScreenIndex > 0
                ) {
                    Text(
                        text = "Назад",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

//                NewDiagnosticProgressBar(newDiagnosticUiState.currentScreenIndex)
                NewDiagnosticProgressBar(currentScreenIndex = newDiagnosticUiState.currentScreenIndex)
                NavHost(
                    navController = navController,
                    startDestination = NewDiagnosticScreen.ImageLoadRoute.route
                ) {
                    composable(NewDiagnosticScreen.ImageLoadRoute.route) {
                        UploadImage(
                            onStartDiagnosticClick = {
                                navController.navigate(
                                    NewDiagnosticScreen.AdditionalInformationRoute.route
                                )
                                newDiagnosticViewModel.onNextScreenButtonClick()
                            },
                            modifier = Modifier.padding(padding)
                        )
                    }

                    composable(NewDiagnosticScreen.AdditionalInformationRoute.route) {
                        AdditionalInformation(
                            newDiagnosticViewModel,
                            modifier = Modifier.padding(padding)
                        )
                    }

                    composable(NewDiagnosticScreen.DiagnosticLoadingRoute.route) {
                        DiagnosticLoading(modifier = Modifier.padding(padding))
                    }
                }
            }
        }
    }

}

sealed class NewDiagnosticScreen(val route: String) {
    object ImageLoadRoute : NewDiagnosticScreen("image_load")
    object AdditionalInformationRoute : NewDiagnosticScreen("additional_information")
    object DiagnosticLoadingRoute : NewDiagnosticScreen("diagnostic_loading")
}

@Preview
@Composable
fun NewDiagnosticNavigationPreview() {
    NewDiagnosticNavigation(
        newDiagnosticViewModel = NewDiagnosticViewModel()
    )
}