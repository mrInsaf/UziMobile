package com.mrinsaf.newdiagnostic.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrinsaf.core.R
import com.mrinsaf.core.data.repository.MockUziServiceRepository
import com.mrinsaf.core.ui.components.LoadingAnimation
import com.mrinsaf.core.ui.components.MainButton
import com.mrinsaf.newdiagnostic.ui.viewModel.DiagnosticProcessState
import com.mrinsaf.newdiagnostic.ui.viewModel.NewDiagnosticViewModel
import com.mrinsaf.newdiagnostic.ui.viewModel.isSuccess

@Composable
fun DiagnosticLoading(
    viewModel: NewDiagnosticViewModel,
    modifier: Modifier = Modifier,
    onDiagnosticCompleted: () -> Unit,
    onUploadNewDiagnostic: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState().value
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        when(uiState.diagnosticProcessState) {
            DiagnosticProcessState.Idle -> TODO()
            DiagnosticProcessState.Sending -> {
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
            is DiagnosticProcessState.Success -> {
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
                MainButton(
                    text = "Открыть результат",
                    enabled = uiState.diagnosticProcessState.isSuccess
                ) {
                    onDiagnosticCompleted()
                }

                MainButton(
                    text = "Загрузить новый снимок",
                ) {
                    onUploadNewDiagnostic()
                }
            }
            DiagnosticProcessState.Failure -> {
                Image(
                    painter = painterResource(R.drawable.close),
                    contentDescription = "",
                    Modifier.size(80.dp)
                )

                Text(
                    text = "Произошла ошибка при диагностике",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Мы уже занимаемся этой проблемой, повторите попытку позже",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.tertiary
                )

                MainButton(
                    text = "Загрузить новый снимок",
                ) {
                    onUploadNewDiagnostic()
                }
            }
        }
    }
}

@Preview
@Composable
fun DiagnosticLoadingPreview() {
    DiagnosticLoading(
        viewModel = NewDiagnosticViewModel(MockUziServiceRepository()),
        modifier = Modifier,
        onDiagnosticCompleted = {},
        onUploadNewDiagnostic = {  }
    )
}

