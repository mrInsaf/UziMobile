package com.example.uzi.ui.screens.newDiagnosticScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.uzi.ui.components.LoadingAnimation
import com.example.uzi.ui.components.MainButton

@Composable
fun DiagnosticLoading(
    modifier: Modifier = Modifier,
    onAndroidBackClick: () -> Unit,
) {
    BackHandler {
        println("back")
        onAndroidBackClick()
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        LoadingAnimation()
        Text(
            text = "Проводится диагностика",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Анализ снимка занимает некоторое время",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.tertiary
        )

        MainButton(
            text = "Открыть результат",
            enabled = false
        ) {
            TODO()
        }
    }
}

@Preview
@Composable
fun DiagnosticLoadingPreview() {
    DiagnosticLoading(
        onAndroidBackClick = {}
    )
}