package com.example.uzi.ui.screens.newDiagnosticScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.uzi.R
import com.example.uzi.ui.components.LoadingAnimation
import com.example.uzi.ui.components.MainButton
import com.example.uzi.ui.viewModel.newDiagnostic.NewDiagnosticViewModel

@Composable
fun DiagnosticLoading(
    viewModel: NewDiagnosticViewModel,
    modifier: Modifier = Modifier,
    onDiagnosticCompleted: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState().value
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        println(uiState.completedDiagnosticId)
        if (uiState.completedDiagnosticId == "") {
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
        }
        else {
            Image(
                painter = painterResource(R.drawable.correct),
                contentDescription = "",
                Modifier.size(80.dp)
            )

            Text(
                text = "Диагностика завершена",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Посмотрите результат, нажав на кнопку",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.tertiary
            )
        }


        MainButton(
            text = "Открыть результат",
            enabled = uiState.completedDiagnosticId != ""
        ) {
            onDiagnosticCompleted()
        }
    }
}

