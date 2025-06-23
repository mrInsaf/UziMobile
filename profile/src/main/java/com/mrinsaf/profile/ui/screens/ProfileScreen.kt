package com.mrinsaf.profile.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.mrinsaf.core.presentation.ui.components.MainButton
import com.mrinsaf.core.presentation.ui.components.fields.ProfileField
import com.mrinsaf.core.presentation.ui.theme.UziTheme
import com.mrinsaf.profile.domain.model.ActiveSubscription
import com.mrinsaf.profile.ui.components.SubscriptionInfoComponent

@Composable
fun ProfileScreen(
    fullName: String?,
    email: String?,
    activeSubscription: ActiveSubscription?,
    hasUserSubscription: Boolean,
    loadUserInfo: suspend () -> Unit,
    fetchSubscriptionInfo: () -> Unit,
    onShowTariffPlans: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    LaunchedEffect(Unit) {
        fetchSubscriptionInfo()
        loadUserInfo()
    }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Профиль",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.tertiary
        )

        ProfileField(
            title = "ФИО",
            textValue = fullName ?: "Ошибка",
        )

        ProfileField(
            title = "Электронная почта",
            textValue = email ?: "Ошибка",
        )

        SubscriptionInfoComponent(
            activeSubscription = activeSubscription,
            hasUserSubscription = hasUserSubscription,
            onShowTariffPlans = { onShowTariffPlans() },
            onSubscribeClick = { onShowTariffPlans() },
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
                onLogoutClick()
            }
        }

    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    UziTheme {
        ProfileScreen(
            fullName = "Алексей Бананов",
            email = "ab@mail.ru",
            loadUserInfo = {},
            fetchSubscriptionInfo = { },
            activeSubscription = ActiveSubscription(
                tariffName = "premium",
                daysUntilExpiration = 2
            ),
            onShowTariffPlans = { },
            onLogoutClick = {},
            hasUserSubscription = true
        )
    }
}