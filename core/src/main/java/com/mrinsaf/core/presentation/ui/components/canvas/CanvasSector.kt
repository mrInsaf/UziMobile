package com.mrinsaf.core.presentation.ui.components.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mrinsaf.core.domain.model.basic.SectorPoint

@Composable
fun CanvasSector(pointsList: List<SectorPoint>) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        pointsList.forEachIndexed { i, point ->
            drawCircle(
                color = Color.Red,
                center = Offset(point.x.toFloat(), point.y.toFloat()),
                radius = 4f
            )
            val nextPoint = if (i < pointsList.size - 1) pointsList[i + 1] else pointsList[0]
            drawLine(
                color = Color.Yellow,
                start = Offset(point.x.toFloat(), point.y.toFloat()),
                end = Offset(nextPoint.x.toFloat(), nextPoint.y.toFloat())
            )
        }
    }
}