package com.mrinsaf.core.presentation.ui.components.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrinsaf.core.presentation.ui.theme.UziTheme

@Composable
fun NewDiagnosticProgressBar(
    currentScreenIndex: Int,
    numberOfScreens: Int = 2,
    circleRadius: Int = 40,
) {
    val circleNumberList = 0..numberOfScreens
    Box(
        modifier = Modifier
            .height(circleRadius.dp)
            .padding(horizontal = 4.dp)
    ) {
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(circleRadius.dp)
        ) {
            val pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(20f, 20f), // Длина отрезка и пробела
                phase = 0f // Смещение начала паттерна
            )
            drawLine(
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2),
                color = Color.LightGray,
                strokeWidth = 5f,
                pathEffect = pathEffect,
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
        ) {
            circleNumberList.forEach {
                CircleNumber(
                    number = it + 1,
                    focused = it == currentScreenIndex,
                    completed = it < currentScreenIndex,
                    size = circleRadius.dp,
                )
            }
        }
    }

}

@Preview
@Composable
fun NewDiagnosticProgressBarPreview() {
    UziTheme {
        NewDiagnosticProgressBar(
            currentScreenIndex = 2
        )
    }
}