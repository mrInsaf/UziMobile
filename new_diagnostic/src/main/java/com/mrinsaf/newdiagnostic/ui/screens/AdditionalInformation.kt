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
import com.mrinsaf.core.presentation.ui.components.MainButton
import com.mrinsaf.core.presentation.ui.components.fields.BasicFormField
import com.mrinsaf.core.presentation.ui.components.fields.dateFormFields.DateFormField
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
