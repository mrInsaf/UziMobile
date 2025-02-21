package com.mrinsaf.core.ui.components.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mrinsaf.core.data.models.basic.Node
import com.mrinsaf.core.ui.components.containers.tirads.EmptyTiradsCotainer
import com.mrinsaf.core.ui.components.containers.tirads.TiradsContainer
import com.mrinsaf.core.ui.theme.Paddings

@Composable
fun DiagnosticListItem(
    date: String,
    clinic: String,
    nodes: List<Node>?,
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
            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = clinic,
                style = MaterialTheme.typography.bodyLarge
            )
            if (!nodes.isNullOrEmpty()){
                nodes.forEach() {
                    TiradsContainer(
                        formationClass = it.formationClass,
                        formationProbability = it.maxTirads.times(100).toInt(),
                        textStyle = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            else {
                EmptyTiradsCotainer(
                    textStyle = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

