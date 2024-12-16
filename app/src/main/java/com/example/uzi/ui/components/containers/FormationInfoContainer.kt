package com.example.uzi.ui.components.containers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uzi.ui.theme.Paddings

@Composable
fun FormationInfoContainer(
    formationIndex: Int,
    formationClass: Int,
    formationProbability: Int,
    formationDescription: String,
) {
    BasicContainer {
        Column(
            verticalArrangement = Arrangement.spacedBy(Paddings.Small),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Образование №$formationIndex",
                color = MaterialTheme.colorScheme.tertiary
            )
            TiradsContainer(formationClass, formationProbability)
            Text(
                text = formationDescription
            )
        }
    }
}



@Preview
@Composable
fun FormationInfoContainerPreview() {
    FormationInfoContainer(
        formationIndex = 1,
        formationClass = 3,
        formationProbability = 80,
        formationDescription = "Вероятно злокачественные изменения ЩЖ"
    )
}