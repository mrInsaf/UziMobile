package com.mrinsaf.core.ui.components.containers.tirads

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun EmptyTiradsCotainer(
    textStyle: TextStyle = MaterialTheme.typography.titleMedium
) {
    BasicTiradsContainer(
        text = "Результатов пока нет",
        textColor = Color(0xFF808080),
        backgroundColor = Color(0xFFF5F5F5),
        textStyle = textStyle,
    )
}