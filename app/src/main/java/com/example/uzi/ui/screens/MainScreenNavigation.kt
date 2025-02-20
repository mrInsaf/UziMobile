package com.example.uzi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.uzi.data.models.User
import com.example.uzi.ui.screens.newDiagnosticScreens.NewDiagnosticNavigation
import com.example.uzi.ui.theme.Paddings
import com.example.uzi.ui.viewModel.diagnostic.DiagnosticViewModel
import com.example.uzi.ui.viewModel.diagnosticList.DiagnosticListViewModel
import com.example.uzi.ui.viewModel.newDiagnostic.NewDiagnosticViewModel
import com.example.uzi.ui.viewModel.newDiagnostic.isSuccess

@Composable
fun MainScreen(
    newDiagnosticViewModel: NewDiagnosticViewModel,
    diagnosticViewModel: DiagnosticViewModel,
    diagnosticListViewModel: DiagnosticListViewModel,
    userData: User,
    patientId: String,
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) },
        modifier = Modifier
            .padding(horizontal = Paddings.Large)
            .background(MaterialTheme.colorScheme.background)
    ) { padding ->
        NavigationGraph(
            navController = navController,
            padding = padding,
            newDiagnosticViewModel = newDiagnosticViewModel,
            diagnosticViewModel = diagnosticViewModel,
            diagnosticListViewModel = diagnosticListViewModel,
            userData = userData,
            patientId = patientId
        )
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    padding: PaddingValues,
    newDiagnosticViewModel: NewDiagnosticViewModel,
    diagnosticViewModel: DiagnosticViewModel,
    diagnosticListViewModel: DiagnosticListViewModel,
    userData: User,
    patientId: String
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Load.route,
        modifier = Modifier.padding(padding)
    ) {
        println("yo")
        composable(Screen.Load.route) {
            val uiState by newDiagnosticViewModel.uiState.collectAsState()

                NewDiagnosticNavigation(
                    newDiagnosticViewModel,
                    onDiagnosticCompleted = {
                        if (uiState.diagnosticProcessState.isSuccess) {
                            diagnosticViewModel.onUziSelected(
                                uziId = uiState.completedDiagnosticId,
                                imagesUris = uiState.downloadedImagesUris,
                                nodesAndSegmentsResponses = uiState.nodesAndSegmentsResponses,
                                uziImages = uiState.uziImages,
                                selectedUziDate = uiState.completedDiagnosticInformation?.createAt ?: "",
                            )
                            navController.navigate(Screen.Diagnostic.route)
                        }
                    }
                )
        }
        composable(Screen.Uploaded.route) {
            val uiState by diagnosticListViewModel.uiState.collectAsState()
            diagnosticListViewModel.getPatientUzis(
                patientId = patientId
            )
            DiagnosticsListScreen(
                uziList = uiState.uziList,
                onDiagnosticListItemClick = { uziId, uziDate ->

                    navController.navigate(Screen.Diagnostic.route)
                },
                nodesWithUziIds = uiState.nodesWithUziId,
            )
        }

        composable(Screen.Account.route) {
            ProfileScreen(
                userName = userData.userName,
                userEmail = userData.userEmail,
            )
        }

        composable(Screen.Diagnostic.route) {
            val diagnosticHistoryUiState by diagnosticViewModel.uiState.collectAsState()
//            diagnosticHistoryViewModel.onUziCompleted(
//                completedDiagnosticId = uiState.completedDiagnosticId,
//                downloadedImagesUris = uiState.downloadedImagesUris,
//                nodesAndSegmentsResponses = uiState.nodesAndSegmentsResponses,
//                uziImages = uiState.uziImages
//            )

            DiagnosticScreen(
                diagnosticDate = diagnosticHistoryUiState.selectedUziDate,
                clinicName = diagnosticHistoryUiState.selectedClinicName ?: "Неизвестная клиника",
                diagnosticViewModel = diagnosticViewModel
            )
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.Load,
        Screen.Uploaded,
        Screen.Account
    )
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        val currentDestination by navController.currentBackStackEntryAsState()
        val currentRoute = currentDestination?.destination?.route
        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(imageVector = screen.icon, contentDescription = screen.label) },
            )
        }
    }
}

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Load : Screen("load", "Загрузить", Icons.Default.Add)
    object Uploaded : Screen("uploaded", "Загруженные", Icons.Default.List)
    object Diagnostic : Screen("diagnostic", "Диагностика", Icons.Default.List)
    object Account : Screen("account", "Аккаунт", Icons.Default.Person)
}

//@Preview
//@Composable
//fun DiagnosticScreensNavigationPreview() {
//    MainScreen(
//        newDiagnosticViewModel = NewDiagnosticViewModel(
//            repository = MockUziServiceRepository()
//        ),
//        userData = User(),
//        diagnosticHistoryViewModel = DiagnosticHistoryViewModel(
//            MockUziServiceRepository()
//        ),
//        patientId = ""
//    )
//}