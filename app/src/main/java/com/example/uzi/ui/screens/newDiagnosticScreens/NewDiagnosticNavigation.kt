package com.example.uzi.ui.screens.newDiagnosticScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uzi.data.repository.MockUziServiceRepository
import com.example.uzi.ui.components.canvas.NewDiagnosticProgressBar
import com.example.uzi.ui.viewModel.newDiagnostic.DiagnosticProcessState
import com.example.uzi.ui.viewModel.newDiagnostic.NewDiagnosticViewModel

@Composable
fun NewDiagnosticNavigation(
    newDiagnosticViewModel: NewDiagnosticViewModel,
    onDiagnosticCompleted: () -> Unit,
) {
    println("yo")
    val newDiagnosticUiState = newDiagnosticViewModel.uiState.collectAsState().value
    val navController = rememberNavController()

    BackHandler(enabled = newDiagnosticUiState.currentScreenIndex > 0) {
        onAndroidBackClick(navController, newDiagnosticViewModel)
    }

    Scaffold(

    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column()
            {
                TextButton(
                    onClick = {
                        onAndroidBackClick(
                            navController = navController,
                            viewModel = newDiagnosticViewModel
                        )
                    },
                    enabled = (newDiagnosticUiState.currentScreenIndex > 0) && (newDiagnosticUiState.diagnosticProcessState !is DiagnosticProcessState.Failure)
                ) {
                    Text(
                        text = "Назад",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
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
                            newDiagnosticViewModel = newDiagnosticViewModel,
                            modifier = Modifier.padding(padding),
                            onAndroidBackClick = {
                                onAndroidBackClick(
                                    navController = navController,
                                    viewModel = newDiagnosticViewModel
                                )
                            },
                        )
                    }

                    composable(NewDiagnosticScreen.AdditionalInformationRoute.route) {
                        AdditionalInformation(
                            newDiagnosticViewModel,
                            modifier = Modifier.padding(padding),
                            onAndroidBackClick = { onAndroidBackClick(
                                navController, newDiagnosticViewModel
                            ) },
                            onNextButtonClick = {
                                newDiagnosticViewModel.onNextScreenButtonClick()
                                navController.navigate(
                                    NewDiagnosticScreen.DiagnosticLoadingRoute.route
                                ) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                                try {
                                    newDiagnosticViewModel.onDiagnosticStart()
                                } catch (e: Exception) {
                                    println(e)
                                }
                            }
                        )
                    }

                    composable(NewDiagnosticScreen.DiagnosticLoadingRoute.route) {
                        DiagnosticLoading(
                            modifier = Modifier.padding(padding),
                            viewModel = newDiagnosticViewModel,
                            onDiagnosticCompleted = onDiagnosticCompleted,
                        )
                    }
                }
            }
        }
    }
}

private fun onAndroidBackClick(
    navController: NavController,
    viewModel: NewDiagnosticViewModel
) {
    if (navController.popBackStack()) {
        viewModel.onPreviousButtonClick()
    }
    println(viewModel.uiState.value.currentScreenIndex)
}

sealed class NewDiagnosticScreen(val route: String) {
    object ImageLoadRoute : NewDiagnosticScreen("image_load")
    object AdditionalInformationRoute : NewDiagnosticScreen("additional_information")
    object DiagnosticLoadingRoute : NewDiagnosticScreen("diagnostic_loading")
}

//@Preview
//@Composable
//fun NewDiagnosticNavigationPreview() {
//    NewDiagnosticNavigation(
//        newDiagnosticViewModel = NewDiagnosticViewModel(
//            repository = MockUziServiceRepository()
//        ),
//        onDiagnosticCompleted = {  }
//    )
//}