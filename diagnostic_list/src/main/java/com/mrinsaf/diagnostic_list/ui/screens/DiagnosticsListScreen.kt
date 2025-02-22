package com.mrinsaf.diagnostic_list.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mrinsaf.core.data.models.basic.NodesWithUziId
import com.mrinsaf.core.data.models.basic.Uzi
import com.mrinsaf.core.ui.components.containers.DiagnosticListItem
import com.mrinsaf.core.ui.theme.Paddings

@Composable
fun DiagnosticsListScreen(
    uziList: List<Uzi>,
    nodesWithUziIds: List<NodesWithUziId>,
    onDiagnosticListItemClick: (String, String) -> Unit,
    fetchPatientUzis: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "История диагностик",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(Modifier.size(Paddings.Large))
        Row(modifier = modifier.fillMaxWidth()) {
            TextButton(
                onClick = { fetchPatientUzis() }
            ) {
                Text("Обновить")
            }
        }
        LazyColumn {
            items(uziList.reversed()) { uzi ->
                val nodes = nodesWithUziIds.find { it.uziId == uzi.id }?.nodes
                DiagnosticListItem(
                    date = uzi.createAt ?: "Неизвестная дата",
                    clinic = "Неизвестная клиника",
                    nodes = nodes,
                    modifier = Modifier
                        .padding(bottom = Paddings.Medium)
                        .clickable {
                            onDiagnosticListItemClick(uzi.id, uzi.createAt,)
                        }
                )
            }
        }
    }
}

@Preview
@Composable
fun DiagnosticsListScreenPreview() {
    DiagnosticsListScreen(
        uziList = emptyList(),
        onDiagnosticListItemClick = { _, _ -> },
        nodesWithUziIds = emptyList(),
        fetchPatientUzis = {  },
    )
}