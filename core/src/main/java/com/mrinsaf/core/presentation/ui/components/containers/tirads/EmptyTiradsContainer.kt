package com.mrinsaf.core.presentation.ui.components.containers.tirads

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.mrinsaf.core.presentation.ui.theme.notActiveBackgroundColor
import com.mrinsaf.core.presentation.ui.theme.notActiveTextColor

@Composable
fun EmptyTiradsCotainer(
    textStyle: TextStyle = MaterialTheme.typography.titleMedium
) {
    BasicTiradsContainer(
        text = "Результатов пока нет",
        textColor = notActiveTextColor,
        backgroundColor = notActiveBackgroundColor,
        textStyle = textStyle,
    )
}