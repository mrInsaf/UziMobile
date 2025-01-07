package com.example.uzi.ui.screens.newDiagnosticScreens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.uzi.data.repository.MockUziServiceRepository
import com.example.uzi.ui.components.MainButton
import com.example.uzi.ui.components.UploadImageComponent
import com.example.uzi.ui.viewModel.newDiagnostic.NewDiagnosticViewModel

@Composable
fun UploadImage(
    onStartDiagnosticClick: () -> Unit,
    onAndroidBackClick: () -> Unit,
    newDiagnosticViewModel: NewDiagnosticViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = newDiagnosticViewModel.uiState.collectAsState()
    val combinedPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                newDiagnosticViewModel.onPhotoPickResult(listOf(it))
            }
        }
    )

    BackHandler {
        println("back")
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
//            fontWeight = FontWeight.Bold
        )
        Text(
            text = helpingText,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )

        UploadImageComponent(
            modifier = Modifier
                .clickable {
                    // Открываем выбор файлов с несколькими типами
                    combinedPickerLauncher.launch(arrayOf("image/*", "image/tiff"))
                }
        )

        MainButton(
            text = "Начать диагностику",
            enabled = uiState.value.selectedImageUris.isNotEmpty()
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
        onStartDiagnosticClick = { },
        onAndroidBackClick = {},
        newDiagnosticViewModel = NewDiagnosticViewModel(
            repository = MockUziServiceRepository()
        ),
    )
}