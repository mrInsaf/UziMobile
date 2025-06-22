package com.example.uzi.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mrinsaf.core.presentation.ui.theme.Paddings
import com.mrinsaf.diagnostic_details.ui.screens.DiagnosticScreen
import com.mrinsaf.diagnostic_details.ui.viewModel.DiagnosticViewModel
import com.mrinsaf.diagnostic_list.ui.screens.DiagnosticsListScreen
import com.mrinsaf.diagnostic_list.ui.viewModel.DiagnosticListViewModel
import com.mrinsaf.newdiagnostic.ui.screens.NewDiagnosticNavigation
import com.mrinsaf.newdiagnostic.ui.viewModel.NewDiagnosticViewModel
import com.mrinsaf.newdiagnostic.ui.viewModel.isSuccess
import com.mrinsaf.profile.ui.screens.ProfileScreen
import com.mrinsaf.profile.ui.screens.ProvidersListScreen
import com.mrinsaf.profile.ui.screens.TariffPlanListScreen
import com.mrinsaf.profile.ui.viewModel.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    newDiagnosticViewModel: NewDiagnosticViewModel,
    diagnosticViewModel: DiagnosticViewModel,
    diagnosticListViewModel: DiagnosticListViewModel,
    profileViewModel: ProfileViewModel,
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
            profileViewModel = profileViewModel,
            patientId = patientId
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    padding: PaddingValues,
    newDiagnosticViewModel: NewDiagnosticViewModel,
    diagnosticViewModel: DiagnosticViewModel,
    diagnosticListViewModel: DiagnosticListViewModel,
    profileViewModel: ProfileViewModel,
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
                        if (downloadedImagesUri != null) {
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
                },
                patientId = patientId
            )
        }
        composable(Screen.Uploaded.route) {
            val uiState by diagnosticListViewModel.uiState.collectAsState()

            LaunchedEffect(Unit){
                diagnosticListViewModel.getPatientUzis(
                    patientId = patientId
                )
            }

            DiagnosticsListScreen(
                uziList = uiState.uziList,
                onDiagnosticListItemClick = { uziId, uziDate ->
                     CoroutineScope(Dispatchers.Main).launch {
                        diagnosticViewModel.onSelectUzi(uziId, uziDate)
                        navController.navigate(Screen.Diagnostic.route)
                    }
                },
                nodesWithUziIds = uiState.nodesWithUziId,
                fetchPatientUzis = { diagnosticListViewModel.getPatientUzis(patientId) },
            )
        }

        composable(Screen.Account.route) {
            val uiState = profileViewModel.uiState.collectAsStateWithLifecycle()
            ProfileScreen(
                fullName = uiState.value.user?.fullName,
                email = uiState.value.user?.email,
                activeSubscription = uiState.value.activeSubscription,
                loadUserInfo = { profileViewModel.loadUserInfo() },
                fetchSubscriptionInfo = { profileViewModel.fetchSubscriptionInfo() },
                onShowTariffPlans = { navController.navigate(Screen.SelectTariff.route) },
            )
        }

        composable(Screen.Diagnostic.route) {
            val diagnosticDetailsUiState by diagnosticViewModel.uiState.collectAsStateWithLifecycle()
            DiagnosticScreen(
                diagnosticDate = diagnosticDetailsUiState.selectedUziDate,
                clinicName = diagnosticDetailsUiState.selectedClinicName ?: "Неизвестная клиника",
                diagnosticViewModel = diagnosticViewModel,
                onBackButtonClick = { navController.popBackStack() }
            )
        }

        composable(Screen.SelectTariff.route) {
            val uiState = profileViewModel.uiState.collectAsStateWithLifecycle()
            TariffPlanListScreen(
                tariffPlanList = uiState.value.tariffPlansList,
                onFetchTariffList = { profileViewModel.fetchTariffPlans() },
                onSelectTariffClick = { 
                    profileViewModel.onSelectTariffClick(it)
                    navController.navigate(Screen.SelectPaymentProvider.route)
                }
            )
        }

        composable(Screen.SelectPaymentProvider.route) {
            val uiState = profileViewModel.uiState.collectAsStateWithLifecycle()
            ProvidersListScreen(
                paymentProviders = uiState.value.paymentProviderList,
                selectedProviderId = uiState.value.selectedProviderId,
                onFetchProviders = { profileViewModel.fetchPaymentProviders() },
                onProviderClick = { profileViewModel.onProviderSelect(it) },
                onContinueClick = { profileViewModel.onPurchaseClick() },
                uiEvent = profileViewModel.uiEvent,
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
                    println("currentRoute: $currentRoute, to screen: ${screen.route}")
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
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
    object SelectTariff : Screen("select_tariff", "Выбор тарифа", Icons.Default.Person)
    object SelectPaymentProvider : Screen("select_payment_provider", "Выбор провайдера", Icons.Default.Person)
}
