package com.mrinsaf.newdiagnostic.ui.screens

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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mrinsaf.core.data.repository.network.MockUziServiceRepository
import com.mrinsaf.core.ui.components.fields.BasicFormField
import com.mrinsaf.core.ui.components.fields.dateFormFields.DateFormField
import com.mrinsaf.core.ui.components.MainButton
import com.mrinsaf.core.ui.components.SaveResultsCheckbox
import com.mrinsaf.newdiagnostic.ui.viewModel.NewDiagnosticViewModel

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
            label = AnnotatedString("Дата приема")
        ) {
            newDiagnosticViewModel.onDatePick(it)
        }

        BasicFormField(
            AdditionalContent = null,
            value = newDiagnosticUiState.clinicName,
            onValueChange = { newDiagnosticViewModel.onClinicNameInput(it) },
            label = "Клиника",
        )

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
            repository = MockUziServiceRepository(),
            context = TODO()
        ),
        onAndroidBackClick = {}
    ) {

    }
}