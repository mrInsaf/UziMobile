package com.example.uzi.ui.components.containers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.uzi.ui.theme.Paddings

@Composable
fun TiradsContainer(
    formationClass: Int,
    formationProbability: Int
) {
    val (textColor, backgroundColor) = when (formationClass) {
        in 1..3 -> Pair(Color(0xFF52c41a), Color(0xFFf6ffed))
        4 -> Pair(Color(0xFFFFA500), Color(0xFFFFF4E5))
        5 -> Pair(Color(0xFFf5222d), Color(0xFFfff1f0))
        else -> Pair(Color.Yellow, Color.Red)
    }
    Box(
        modifier = Modifier
            .background(color = backgroundColor)
            .border(width = 1.dp, color = textColor)
            .padding(Paddings.ExtraSmall)
    ) {
        Text(
            text = "EU TIRADS $formationClass - $formationProbability%",
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )
    }
}