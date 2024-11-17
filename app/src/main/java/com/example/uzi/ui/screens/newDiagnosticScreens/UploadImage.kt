package com.example.uzi.ui.screens.newDiagnosticScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.uzi.ui.components.MainButton
import com.example.uzi.ui.components.UploadImageComponent

@Composable
fun UploadImage(
    onStartDiagnosticClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
//            fontWeight = FontWeight.Bold
        )
        Text(
            text = helpingText,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )

        UploadImageComponent()

        MainButton(
            text = "Начать диагностику"
        ) {
            onStartDiagnosticClick()
        }
    }
}

val helpingText = "Для начала диагностики загрузите " +
        "интересующий снимок ультразвуковой диагностики " +
        "щитовидной железы"

@Preview
@Composable
fun UploadImagePreview() {
    UploadImage(
        onStartDiagnosticClick = {  }
    )
}