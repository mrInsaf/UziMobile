package com.example.uzi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.uzi.data.models.User
import com.example.uzi.data.repository.MockUziServiceRepository
import com.example.uzi.ui.screens.newDiagnosticScreens.NewDiagnosticNavigation
import com.example.uzi.ui.theme.Paddings
import com.example.uzi.ui.viewModel.diagnosticHistory.DiagnosticHistoryViewModel
import com.example.uzi.ui.viewModel.newDiagnostic.NewDiagnosticViewModel

@Composable
fun MainScreen(
    newDiagnosticViewModel: NewDiagnosticViewModel,
    diagnosticHistoryViewModel: DiagnosticHistoryViewModel,
    userData: User,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = Paddings.Large)
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        val newDiagnosticUiState = newDiagnosticViewModel.uiState.collectAsState().value
        val diagnosticHistoryUiState = diagnosticHistoryViewModel.uiState.collectAsState().value
        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController = navController)
            },
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Load.route,
                modifier = Modifier
                    .padding(padding)
            ) {
                composable(Screen.Load.route) {
                    NewDiagnosticNavigation(
                        newDiagnosticViewModel,
                        onDiagnosticCompleted = {
//                            diagnosticHistoryViewModel.addUziId(newDiagnosticUiState.completedDiagnosticId) TODO Переделать на сохранение в локальное хранилище
                            diagnosticHistoryViewModel.onSelectUzi(
                                completedDiagnosticId = newDiagnosticUiState.completedDiagnosticId,
                                downloadedImagesUris = newDiagnosticUiState.downloadedImagesUris,
                                nodesAndSegmentsResponse = newDiagnosticUiState.nodesAndSegmentsResponse,
                                uziImages = newDiagnosticUiState.uziImages
                            )
                            navController.navigate(Screen.Uploaded.route)
                        }
                    )
                }
                composable(Screen.Uploaded.route) {
                    DiagnosticScreen(
                        diagnosticDate = "Дата",
                        clinicName = "Клиника",
                        diagnosticHistoryViewModel = diagnosticHistoryViewModel
                    )
                }
                composable(Screen.Account.route) {
                    ProfileScreen(
                        userName = userData.userName,
                        userEmail = userData.userEmail,
                    )
                }
            }
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
    object Account : Screen("account", "Аккаунт", Icons.Default.Person)
}

@Preview
@Composable
fun DiagnosticScreensNavigationPreview() {
    MainScreen(
        newDiagnosticViewModel = NewDiagnosticViewModel(
            repository = MockUziServiceRepository()
        ),
        userData = User(),
        diagnosticHistoryViewModel = DiagnosticHistoryViewModel(
            MockUziServiceRepository()
        )
    )
}