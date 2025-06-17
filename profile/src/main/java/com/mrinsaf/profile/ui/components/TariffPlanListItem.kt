package com.mrinsaf.profile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrinsaf.core.data.network.dto.network_responses.TariffPlanResponse
import com.mrinsaf.core.presentation.ui.components.containers.BasicContainer
import com.mrinsaf.core.presentation.ui.theme.Paddings
import com.mrinsaf.core.presentation.ui.theme.UziTheme

@Composable
fun TariffPlanListItem(
    tariffName: String,
    description: String,
) {
    BasicContainer {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .fillMaxWidth()
        ) {
            Spacer(Modifier.size(Paddings.ExtraLarge))
            Text(
                text = tariffName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.size(Paddings.Medium))

            val descriptionParagraphs = description.split(';')

            BulletPointList(descriptionParagraphs)

        }
    }
}

@Composable
fun BulletPointList(items: List<String>) {
    Column {
        items.forEach { item ->
            Row(
                modifier = Modifier.padding(vertical = Paddings.ExtraSmall)
            ) {
                Text(
                    text = "•",
                    modifier = Modifier.padding(end = 8.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = item,
                )
            }
        }
    }
}

@Preview
@Composable
fun TariffPlanListItemPreview() {
    UziTheme {
        TariffPlanListItem(
            tariffName = "5 снимков в неделю",
            description = "Загрузка снимков в любом формате JPEG, PNG, TIFF, DICOM;Ведение карт пациентов;Консультация с экспертами системы (более 10 лет медицинской практики)",
        )
    }
}