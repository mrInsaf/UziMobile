package com.example.uzi.ui.screens.newDiagnosticScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.uzi.ui.components.BasicFormField
import com.example.uzi.ui.components.DateFormField
import com.example.uzi.ui.components.MainButton
import com.example.uzi.ui.viewModel.newDiagnostic.NewDiagnosticViewModel

@Composable
fun AdditionalInformation(
    newDiagnosticViewModel: NewDiagnosticViewModel,
    modifier: Modifier = Modifier
) {
    val newDiagnosticUiState = newDiagnosticViewModel.uiState.collectAsState().value
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Text(
            text= "Дополнительные данные",
            style = MaterialTheme.typography.titleLarge,
        )
        DateFormField(
            label = "Дата приема"
        ) {
            newDiagnosticViewModel.onDatePick(it)
        }

//        BasicFormField(
//            AdditionalContent = null,
//            value = TODO(),
//            onValueChange = TODO(),
//            label = "Клиника",
//        )
//
//        Checkbox(
//            checked = TODO(),
//            onCheckedChange = TODO(),
//            modifier = TODO(),
//            enabled = TODO(),
//        )
//
//        MainButton(
//            text = "Далее"
//        ) { }
    }
}

@Preview
@Composable
fun AdditionalInformationPreview() {
    AdditionalInformation(NewDiagnosticViewModel())
}