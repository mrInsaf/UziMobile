package com.example.uzi.ui

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mrinsaf.core.data.models.User
import com.mrinsaf.diagnostic_list.ui.screens.DiagnosticsListScreen
import com.mrinsaf.core.ui.screens.ProfileScreen
import com.mrinsaf.core.ui.theme.Paddings
import com.mrinsaf.diagnostic_details.ui.screens.DiagnosticScreen
import com.mrinsaf.diagnostic_details.ui.viewModel.DiagnosticViewModel
import com.mrinsaf.diagnostic_list.ui.viewModel.DiagnosticListViewModel
import com.mrinsaf.newdiagnostic.ui.screens.NewDiagnosticNavigation
import com.mrinsaf.newdiagnostic.ui.viewModel.NewDiagnosticViewModel
import com.mrinsaf.newdiagnostic.ui.viewModel.isSuccess
import kotlinx.coroutines.launch


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
        composable(Screen.Load.route) {
            val uiState by newDiagnosticViewModel.uiState.collectAsState()

            NewDiagnosticNavigation(
                newDiagnosticViewModel,
                onDiagnosticCompleted = {
                    if (uiState.diagnosticProcessState.isSuccess) {
                        val downloadedImagesUri = uiState.downloadedImagesUri
                        if (downloadedImagesUri != null){
                            diagnosticViewModel.onDiagnosticCompleted(
                                uziId = uiState.completedDiagnosticId,
                                imagesUris = downloadedImagesUri,
                                nodesAndSegmentsResponses = uiState.nodesAndSegmentsResponses,
                                uziImages = uiState.uziImages,
                                selectedUziDate = uiState.completedDiagnosticInformation?.createAt
                                    ?: "",
                                uziImagesBmp = uiState.uziImagesBmp,
                            )
                        }
                        navController.navigate(Screen.Diagnostic.route)
                    }
                }
            )
        }
        composable(Screen.Uploaded.route) {
            val uiState by diagnosticListViewModel.uiState.collectAsState()
            val coroutineScope = rememberCoroutineScope()
//            diagnosticViewModel.clearUiState()
            diagnosticListViewModel.getPatientUzis(
                patientId = patientId
            )
            DiagnosticsListScreen(
                uziList = uiState.uziList,
                onDiagnosticListItemClick = { uziId, uziDate ->
                    coroutineScope.launch {
                        diagnosticViewModel.onSelectUzi(uziId, uziDate)
                        navController.navigate(Screen.Diagnostic.route)
                    }

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