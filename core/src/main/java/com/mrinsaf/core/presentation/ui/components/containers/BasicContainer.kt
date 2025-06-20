package com.mrinsaf.core.presentation.ui.components.containers

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrinsaf.core.presentation.ui.theme.Paddings
import com.mrinsaf.core.presentation.ui.theme.UziShapes

@Composable
fun BasicContainer(
    modifier: Modifier = Modifier,
    borderColor: Color = Color.LightGray,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier
        .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(UziShapes.ContainerCornerRadius))
        .padding(Paddings.Medium)
    ) {
        content()
    }
}

@Preview
@Composable
fun BasicContainerPreview() {
    BasicContainer { Text("y\noACSDCASD") }
}