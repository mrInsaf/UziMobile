package com.mrinsaf.profile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mrinsaf.core.ui.components.MainButton
import com.mrinsaf.core.ui.components.fields.ProfileField
import com.mrinsaf.profile.ui.viewModel.ProfileViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
) {
    LaunchedEffect(Unit) {
        profileViewModel.loadUserInfo()
    }

    val uiState = profileViewModel.uiState.collectAsStateWithLifecycle()
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Профиль",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.tertiary
        )

        ProfileField(
            title = "ФИО",
            content = uiState.value.user?.fullName ?: "Ошибка",
        )

        ProfileField(
            title = "Электронная почта",
            content = uiState.value.user?.email ?: "Ошибка",
        )

        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            MainButton(
                text = "Выйти из аккаунта",
                containerColor = Color(0xFFf5222d)
            ) {
                println("logout")
            }
        }

    }
}