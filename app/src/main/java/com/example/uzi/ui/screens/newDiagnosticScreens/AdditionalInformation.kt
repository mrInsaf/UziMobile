package com.example.uzi.ui.screens.newDiagnosticScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.uzi.data.repository.MockUziServiceRepository
import com.example.uzi.ui.components.fields.BasicFormField
import com.example.uzi.ui.components.fields.DateFormField
import com.example.uzi.ui.components.MainButton
import com.example.uzi.ui.components.SaveResultsCheckbox
import com.example.uzi.ui.viewModel.newDiagnostic.NewDiagnosticViewModel

@Composable
fun AdditionalInformation(
    newDiagnosticViewModel: NewDiagnosticViewModel,
    modifier: Modifier = Modifier,
    onAndroidBackClick: () -> Unit,
    onNextButtonClick: () -> Unit,
) {
    val newDiagnosticUiState = newDiagnosticViewModel.uiState.collectAsState().value
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
        Text(
            text= "Дополнительные данные",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        DateFormField(
            label = "Дата приема"
        ) {
            newDiagnosticViewModel.onDatePick(it)
        }

        BasicFormField(
            AdditionalContent = null,
            value = newDiagnosticUiState.clinicName,
            onValueChange = { newDiagnosticViewModel.onClinicNameInput(it) },
            label = "Клиника",
        )

        SaveResultsCheckbox(
            isChecked = newDiagnosticUiState.saveResultsChecked
        ) {
            newDiagnosticViewModel.onSaveResultsCheck()
        }

        MainButton(
            text = "Далее"
        ) {
            try {
                onNextButtonClick()
            } catch (e: Exception) {
                println(e)
            }
        }
    }
}

@Preview
@Composable
fun AdditionalInformationPreview() {
    AdditionalInformation(
        NewDiagnosticViewModel(
            repository = MockUziServiceRepository()
        ),
        onAndroidBackClick = {}
    ) {

    }
}