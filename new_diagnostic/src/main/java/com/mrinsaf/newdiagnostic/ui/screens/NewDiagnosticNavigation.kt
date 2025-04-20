package com.mrinsaf.newdiagnostic.ui.screens

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mrinsaf.core.ui.components.canvas.NewDiagnosticProgressBar
import com.mrinsaf.newdiagnostic.ui.viewModel.DiagnosticProcessState
import com.mrinsaf.newdiagnostic.ui.viewModel.NewDiagnosticViewModel

@Composable
fun NewDiagnosticNavigation(
    newDiagnosticViewModel: NewDiagnosticViewModel,
    onDiagnosticCompleted: () -> Unit,
) {
    val newDiagnosticUiState = newDiagnosticViewModel.uiState.collectAsState().value
    val navController = rememberNavController()

    val combinedPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                newDiagnosticViewModel.onPhotoPickResult(it)
            }
        }
    )

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
                            onUploadImageClick = {
                                combinedPickerLauncher.launch(arrayOf("image/*", "image/tiff"))
                            }
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
                            onUploadNewDiagnostic = {
                                try {
                                    navController.navigate(NewDiagnosticScreen.ImageLoadRoute.route) {
                                        popUpTo(0)
                                    }
                                    newDiagnosticViewModel.returnToUploadScreen()
                                } catch (e: Exception) {
                                    println(e)
                                }
                            },
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