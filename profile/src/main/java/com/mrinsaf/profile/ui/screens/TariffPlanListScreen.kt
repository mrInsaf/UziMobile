package com.mrinsaf.profile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mrinsaf.core.presentation.ui.components.LoadingAnimation
import com.mrinsaf.core.presentation.ui.components.MainButton
import com.mrinsaf.core.presentation.ui.screen.BasicScreen
import com.mrinsaf.core.presentation.ui.theme.Paddings
import com.mrinsaf.core.presentation.ui.theme.UziTheme
import com.mrinsaf.core.presentation.ui.theme.successTextColor
import com.mrinsaf.profile.R
import com.mrinsaf.profile.domain.model.TariffPlan
import com.mrinsaf.profile.ui.components.HorizontalPagerIndicator
import com.mrinsaf.profile.ui.components.TariffPlanListItem

@Composable
fun TariffPlanListScreen(
    tariffPlanList: List<TariffPlan> = emptyList(),
    onFetchTariffList: () -> Unit,
    onSelectTariffClick: (String) -> Unit,
) {

    LaunchedEffect(Unit) {
        onFetchTariffList()
    }
    
    BasicScreen(
        title = stringResource(R.string.subscriptions),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (tariffPlanList.isNotEmpty()) {
                val pagerState = rememberPagerState(pageCount = {
                    tariffPlanList.size
                })

                HorizontalPager(
                    state = pagerState,
                    pageSpacing = Paddings.Medium
                ) { page ->
                    TariffPlanListItem(
                        tariffName = tariffPlanList[page].name,
                        description = tariffPlanList[page].description,
                    )
                }

                Spacer(Modifier.size(Paddings.Medium))

                HorizontalPagerIndicator(
                    pageCount = tariffPlanList.size,
                    currentPage = pagerState.currentPage
                )

                Spacer(Modifier.size(Paddings.Medium))

                MainButton(
                    text = "Оформить подписку\nза ${tariffPlanList[pagerState.currentPage].price}/мес",
                    containerColor = successTextColor,
                    onClick = {
                        onSelectTariffClick(tariffPlanList[pagerState.currentPage].id)
                    }
                )
            }
            else {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    LoadingAnimation()
                }
            }
        }
    }
}

@Preview
@Composable
fun TariffPlanListScreenPreview() {
    UziTheme {
        TariffPlanListScreen(
            tariffPlanList = listOf(
                TariffPlan(
                    name = "Базовый",
                    description = "Неограниченные звонки по России;SMS — 100 штук в месяц;Интернет — до 10 Гб/мес",
                    price = "399 ₽",
                    id = "1"
                ),
                TariffPlan(
                    name = "Стандартный",
                    description = "Неограниченные звонки по России и СНГ;SMS — 300 штук в месяц;Интернет — до 25 Гб/мес",
                    price = "599 ₽",
                    id = "2"
                ),
                TariffPlan(
                    name = "Премиум",
                    description = "Неограниченные звонки по всему миру;SMS — безлимит;Интернет — до 50 Гб/мес;Подписка на стриминговый сервис в подарок",
                    price = "999 ₽",
                    id = "3"
                ),
                TariffPlan(
                    name = "Максимальный",
                    description = "Неограниченные звонки и SMS по всему миру;Интернет — безлимит;VIP-поддержка;Доступ к эксклюзивному контенту",
                    price = "1499 ₽",
                    id = "4"
                )
            ),
            onFetchTariffList = { },
            onSelectTariffClick = {  },
        )
    }
}