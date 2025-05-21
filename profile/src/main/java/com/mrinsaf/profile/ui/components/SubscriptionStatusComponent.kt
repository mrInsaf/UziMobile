package com.mrinsaf.profile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrinsaf.core.presentation.ui.components.containers.BasicContainer
import com.mrinsaf.core.presentation.ui.components.containers.BasicStatusContainer
import com.mrinsaf.core.presentation.ui.theme.UziTheme
import com.mrinsaf.core.presentation.ui.theme.notActiveBackgroundColor
import com.mrinsaf.core.presentation.ui.theme.notActiveTextColor
import com.mrinsaf.core.presentation.ui.theme.successBackgroundColor
import com.mrinsaf.core.presentation.ui.theme.successTextColor
import com.mrinsaf.profile.R


@Composable
fun SubscriptionStatusComponent(
    isSubscriptionActive: Boolean
) {
    val containerParams = when {
        isSubscriptionActive -> ContainerParams(
            textColor = successTextColor,
            backgroundColor = successBackgroundColor,
            text = stringResource(R.string.subscription_is_active)
        )
        else -> ContainerParams(
            textColor = notActiveTextColor,
            backgroundColor = notActiveBackgroundColor,
            text = stringResource(R.string.subscription_not_active)
        )
    }
    BasicStatusContainer(
        textColor = containerParams.textColor,
        backgroundColor = containerParams.backgroundColor
    ) {
        Text(
            text = containerParams.text,
            style = MaterialTheme.typography.bodyMedium,
            color = containerParams.textColor
        )
    }
}

@Preview
@Composable
fun SubscriptionStatusComponentPreview() {
    UziTheme {
        SubscriptionStatusComponent(
            isSubscriptionActive = false,
        )
    }
}

private data class ContainerParams(
    val textColor: Color,
    val backgroundColor: Color,
    val text: String,
)