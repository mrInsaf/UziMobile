package com.mrinsaf.core.presentation.ui.components.containers.tirads

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun TiradsContainer(
    formationClass: Int,
    formationProbability: Int,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium
) {
    val (textColor, backgroundColor) = when (formationClass) {
        in 1..3 -> Pair(Color(0xFF52c41a), Color(0xFFf6ffed))
        4 -> Pair(Color(0xFFFFA500), Color(0xFFFFF4E5))
        5 -> Pair(Color(0xFFf5222d), Color(0xFFfff1f0))
        else -> Pair(Color.Yellow, Color.Red)
    }
    BasicTiradsContainer(
        text = "EU TIRADS $formationClass - $formationProbability%",
        textColor = textColor,
        backgroundColor = backgroundColor,
        textStyle = textStyle
    )
}