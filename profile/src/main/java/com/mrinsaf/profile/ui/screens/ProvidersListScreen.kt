package com.mrinsaf.profile.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrinsaf.core.R
import com.mrinsaf.core.presentation.event.model.UiEvent
import com.mrinsaf.core.presentation.ui.components.MainButton
import com.mrinsaf.core.presentation.ui.components.containers.BasicContainer
import com.mrinsaf.core.presentation.ui.screen.BasicScreen
import com.mrinsaf.core.presentation.ui.theme.Paddings
import com.mrinsaf.core.presentation.ui.theme.UziShapes
import com.mrinsaf.core.presentation.ui.theme.UziTheme
import com.mrinsaf.core.presentation.ui.theme.successTextColor
import com.mrinsaf.profile.domain.model.PaymentProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow


@Composable
fun ProvidersListScreen(
    paymentProviders: List<PaymentProvider>,
    selectedProviderId: String?,
    uiEvent: SharedFlow<UiEvent>,
    onFetchProviders: () -> Unit,
    onProviderClick: (String) -> Unit,
    onContinueClick: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        onFetchProviders()

        uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, uiEvent.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    BasicScreen(
        title = "Провайдеры",
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            paymentProviders.forEach { provider ->
                BasicContainer(
                    borderColor = if (provider.id == selectedProviderId) successTextColor else Color.LightGray,
                    modifier = Modifier
                        .clip(RoundedCornerShape(UziShapes.ContainerCornerRadius))
                        .clickable(
                            onClick = { onProviderClick(provider.id) }
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.time),
                            contentDescription = "",
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.size(Paddings.Medium))

                        Column(
                            verticalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Text(
                                text = provider.name,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            provider.description?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.DarkGray
                                )
                            }
                        }

                    }
                }

            }

            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                MainButton(
                    text = "Продолжить",
                    containerColor = successTextColor,
                    enabled = selectedProviderId != null
                ) {
                    onContinueClick()
                }
            }
        }
    }
}

@Preview
@Composable
fun ProvidersListScreenPreview() {
    UziTheme {
        ProvidersListScreen(
            paymentProviders = listOf(
                PaymentProvider(
                    id = "1",
                    name = "Юкасса",
                    description = "Очень крутое описание",
                    isActive = true,
                )
            ),
            selectedProviderId = null,
            onProviderClick = {},
            onFetchProviders = {},
            onContinueClick = {},
            uiEvent = MutableSharedFlow<UiEvent>()
        )
    }
}