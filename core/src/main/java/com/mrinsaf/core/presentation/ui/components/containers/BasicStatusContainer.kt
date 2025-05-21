package com.mrinsaf.core.presentation.ui.components.containers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mrinsaf.core.presentation.ui.theme.Paddings

@Composable
fun BasicStatusContainer(
    textColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .background(color = backgroundColor)
            .border(width = 1.dp, color = textColor)
            .padding(Paddings.ExtraSmall)
    ) {
        content()
    }
}