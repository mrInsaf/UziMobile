package com.example.uzi.ui.components.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.uzi.data.models.Formation
import com.example.uzi.data.models.Tirads
import com.example.uzi.ui.theme.Paddings

@Composable
fun DiagnosticListItem(
    date: String,
    clinic: String,
    formations: List<Formation>,
    modifier: Modifier = Modifier
) {
    BasicContainer(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Paddings.ExtraSmall),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(date)
            Text(clinic)
            formations.forEach() {
                TiradsContainer(
                    formationClass = it.formationClass,
                    formationProbability = it.maxTirads,
                )
            }
        }

    }
}

@Preview
@Composable
fun DiagnosticListItemPreview() {
    DiagnosticListItem(
        date = "12.12.2024",
        clinic = "Клиника",
        formations = List(3) { index ->
            Formation(
                ai = index % 2 == 0, // Пример: чередование AI-обнаружения
                id = "formation_${index + 1}",
                tirads = Tirads(
                    tirads_23 = (20..40).random(),
                    tirads_4 = (10..30).random(),
                    tirads_5 = (5..15).random()
                )
            )
        }
    )
}