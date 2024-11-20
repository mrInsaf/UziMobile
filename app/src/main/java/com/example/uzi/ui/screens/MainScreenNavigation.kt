package com.example.uzi.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.uzi.ui.screens.newDiagnosticScreens.NewDiagnosticNavigation
import com.example.uzi.ui.viewModel.newDiagnostic.NewDiagnosticViewModel

@Composable
fun MainScreen(
    newDiagnosticViewModel: NewDiagnosticViewModel
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { padding ->
        NavHost(
            navController = navController, 
            startDestination = Screen.Load.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Load.route) {
                NewDiagnosticNavigation(newDiagnosticViewModel)
            }
            composable(Screen.Uploaded.route) { TODO() }
            composable(Screen.Account.route) { TODO() }
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
//                label = { Text(screen.label) }
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
        newDiagnosticViewModel = NewDiagnosticViewModel()
    )
}