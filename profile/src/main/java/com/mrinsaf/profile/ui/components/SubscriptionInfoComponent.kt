package com.mrinsaf.profile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mrinsaf.core.presentation.ui.components.MainButton
import com.mrinsaf.core.presentation.ui.components.fields.ProfileField
import com.mrinsaf.core.presentation.ui.theme.Paddings
import com.mrinsaf.core.presentation.ui.theme.UziTheme
import com.mrinsaf.profile.R

@Composable
fun SubscriptionInfoComponent(
    subscriptionDaysRemaining: Int?,
    onShowTariffPlans: () -> Unit,
    onSubscribeClick: () -> Unit,
) {
    ProfileField(
        title = stringResource(R.string.subscription_title),
        content = {
            SubscriptionComponentContent(
                subscriptionDaysRemaining = subscriptionDaysRemaining,
                onShowTariffPlans = onShowTariffPlans,
                onSubscribeClick = onSubscribeClick,
            )
        }
    )
}

@Composable
fun SubscriptionComponentContent(
    subscriptionDaysRemaining: Int?,
    onShowTariffPlans: () -> Unit,
    onSubscribeClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Paddings.ExtraSmall),
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        val isSubscriptionActive = subscriptionDaysRemaining?.let {
            it > 0
        } ?: false
        SubscriptionStatusComponent(isSubscriptionActive)
        subscriptionDaysRemaining?.let {
            Text(text = "Осталось дней: $subscriptionDaysRemaining")
        }

        TextButton(onClick = {
            onShowTariffPlans()
        }) {
            Text(stringResource(R.string.show_tariff_plans))
        }

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            MainButton(
                text = stringResource(R.string.subscribe),
                enabled = !isSubscriptionActive
            ) {
                onSubscribeClick()
            }
        }
    }
}

@Preview
@Composable
fun SubscriptionInfoComponentPreview() {
    UziTheme {
        SubscriptionInfoComponent(
            subscriptionDaysRemaining = 0,
            onShowTariffPlans = { },
            onSubscribeClick = { },
        )
    }
}