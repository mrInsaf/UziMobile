package com.mrinsaf.core.presentation.ui.components.containers.tirads

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mrinsaf.core.presentation.ui.components.containers.BasicStatusContainer
import com.mrinsaf.core.presentation.ui.theme.Paddings

@Composable
fun BasicTiradsContainer(
    text: String,
    textColor: Color,
    backgroundColor: Color,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    modifier: Modifier = Modifier
) {
    BasicStatusContainer(
        textColor = textColor,
        backgroundColor = backgroundColor,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = textStyle,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}