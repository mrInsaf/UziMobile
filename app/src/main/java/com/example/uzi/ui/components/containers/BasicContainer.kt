package com.example.uzi.ui.components.containers

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uzi.ui.theme.Paddings

@Composable
fun BasicContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier
        .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(20.dp))
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