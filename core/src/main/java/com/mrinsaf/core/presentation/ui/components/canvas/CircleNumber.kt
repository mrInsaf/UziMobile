package com.mrinsaf.core.presentation.ui.components.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CircleNumber(
    number: Int,
    size: Dp = 40.dp,
    focused: Boolean,
    completed: Boolean,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size)
    ) {
        val backgroundColor = MaterialTheme.colorScheme.background
        val focusedColor = MaterialTheme.colorScheme.secondary
        val completedColor = MaterialTheme.colorScheme.tertiary
        val onBackgroundColor = MaterialTheme.colorScheme.onBackground
        val onFocusedColor = MaterialTheme.colorScheme.onSecondary
        // Рисуем круг
        Canvas(modifier = Modifier
            .matchParentSize()
        ) {
            var color: Color = focusedColor
            var style: DrawStyle = Fill
            when {
                completed -> {
                    color = completedColor
                }
                focused -> {
                    color = focusedColor
                }
                else -> {
                    style = Stroke(width = 5f)
                    drawCircle(
                        color = backgroundColor,
                        style = Fill,
                        radius = size.toPx() / 2 - 1,
                    )
                }
            }
            drawCircle(
                color = color,
                style = style,
                radius = size.toPx() / 2,
            )
//            if (!focused && !completed) {
//                drawCircle(
//                    color = Color.White,
//                    style = Fill,
//                    radius = size.toPx() / 2 - 1,
//                )
//            }
        }

        // Отображаем число
        Text(
            text = number.toString(),
            color = if(focused) onFocusedColor else onBackgroundColor,
            fontSize = 20.sp,
        )
    }
}

@Preview
@Composable
fun CircleNumberPreview() {
    CircleNumber(
        5,
        focused = false,
        completed = false
    )
}
