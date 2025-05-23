package com.mrinsaf.newdiagnostic.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.mrinsaf.core.presentation.ui.components.MainButton
import com.mrinsaf.core.presentation.ui.components.UploadImageComponent
import com.mrinsaf.newdiagnostic.ui.viewModel.NewDiagnosticViewModel

@Composable
fun UploadImage(
    onStartDiagnosticClick: () -> Unit,
    onAndroidBackClick: () -> Unit,
    onUploadImageClick: () -> Unit,
    newDiagnosticViewModel: NewDiagnosticViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = newDiagnosticViewModel.uiState.collectAsState()

    BackHandler {
        println("UploadImage: BackHandler triggered")
        onAndroidBackClick()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Text(
            text = "Загрузка снимка",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = helpingText,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )

        UploadImageComponent(
            modifier = Modifier
                .clickable {
                    println("UploadImage: UploadImageComponent clicked")
                    onUploadImageClick()
                },
            fileName = uiState.value.selectedImageName
        )

        MainButton(
            text = "Начать диагностику",
            enabled = uiState.value.selectedImageUri != null
        ) {
            onStartDiagnosticClick()
        }
    }
}

val helpingText = "Для начала диагностики загрузите " +
        "интересующий снимок ультразвуковой диагностики " +
        "щитовидной железы"
