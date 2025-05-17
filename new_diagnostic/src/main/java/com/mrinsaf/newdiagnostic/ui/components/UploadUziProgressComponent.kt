package com.mrinsaf.newdiagnostic.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UploadUziProgressComponent(
    currentProgress: Float,
    progressStateMessage: String,
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        LinearProgressIndicator(
            progress = { currentProgress },
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = progressStateMessage,
            style = MaterialTheme.typography.bodySmall,
            color = Color.LightGray
        )
    }
}