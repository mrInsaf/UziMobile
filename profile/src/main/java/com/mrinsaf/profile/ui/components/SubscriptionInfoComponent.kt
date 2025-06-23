package com.mrinsaf.profile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.size.Size
import com.mrinsaf.core.presentation.ui.components.LoadingAnimation
import com.mrinsaf.core.presentation.ui.components.MainButton
import com.mrinsaf.core.presentation.ui.components.fields.ProfileField
import com.mrinsaf.core.presentation.ui.theme.Paddings
import com.mrinsaf.core.presentation.ui.theme.UziTheme
import com.mrinsaf.profile.R
import com.mrinsaf.profile.domain.model.ActiveSubscription

@Composable
fun SubscriptionInfoComponent(
    activeSubscription: ActiveSubscription?,
    onShowTariffPlans: () -> Unit,
    onSubscribeClick: () -> Unit,
    hasUserSubscription: Boolean,
    isCheckingSubscriptionAfterPurchase: Boolean
) {
    ProfileField(
        title = stringResource(R.string.subscription_title),
        content = {
            SubscriptionComponentContent(
                activeSubscription = activeSubscription,
                onShowTariffPlans = onShowTariffPlans,
                onSubscribeClick = onSubscribeClick,
                hasUserSubscription = hasUserSubscription,
                isCheckingSubscriptionAfterPurchase = isCheckingSubscriptionAfterPurchase
            )
        }
    )
}

@Composable
fun SubscriptionComponentContent(
    hasUserSubscription: Boolean,
    activeSubscription: ActiveSubscription?,
    isCheckingSubscriptionAfterPurchase: Boolean,
    onShowTariffPlans: () -> Unit,
    onSubscribeClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Paddings.ExtraSmall),
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SubscriptionStatusComponent(hasUserSubscription)
            Spacer(Modifier.size(Paddings.Medium))
            if (isCheckingSubscriptionAfterPurchase) {
                LoadingAnimation(
                    size = Size(120, 120)
                )
            }
        }
        activeSubscription?.let {
            Text(text = "Название: ${it.tariffName}")
            Text(text = "Осталось дней: ${it.daysUntilExpiration}")
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
                enabled = !hasUserSubscription
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
            onShowTariffPlans = { },
            onSubscribeClick = { },
            activeSubscription = ActiveSubscription("premium", 2),
            hasUserSubscription = true,
            isCheckingSubscriptionAfterPurchase = false,
        )
    }
}