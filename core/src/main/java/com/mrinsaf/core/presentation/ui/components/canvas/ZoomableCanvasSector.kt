package com.mrinsaf.core.presentation.ui.components.canvas

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntSize
import com.mrinsaf.core.domain.model.basic.SectorPoint

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ZoomableCanvasSector(
    imageBitmap: ImageBitmap,
    pointsList: List<SectorPoint>,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val aspectRatio = imageBitmap.width.toFloat() / imageBitmap.height.toFloat()

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
    ) {
        val state = rememberTransformableState { zoomChange, panChange, _ ->
            scale = (scale * zoomChange).coerceIn(1f, 5f)

            val extraWidth = (scale - 1) * constraints.maxWidth
            val extraHeight = (scale - 1) * constraints.maxHeight

            val maxX = extraWidth / 2
            val maxY = extraHeight / 2

            offset = Offset(
                x = (offset.x + scale * panChange.x).coerceIn(-maxX, maxX),
                y = (offset.y + scale * panChange.y).coerceIn(-maxY, maxY),
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .transformable(state)
        ) {
            clipRect {
                val canvasWidth = size.width.toInt()
                val canvasHeight = size.height.toInt()

                drawImage(
                    image = imageBitmap,
                    dstSize = IntSize(canvasWidth, canvasHeight),
                )

                val scaleX = size.width / imageBitmap.width
                val scaleY = size.height / imageBitmap.height

                pointsList.forEachIndexed { i, point ->
                    val scaledX = point.x * scaleX
                    val scaledY = point.y * scaleY

                    drawCircle(
                        color = Color.Red,
                        center = Offset(scaledX, scaledY),
                        radius = 0f
                    )

                    val nextPoint = if (i < pointsList.size - 1) pointsList[i + 1] else pointsList[0]
                    val nextScaledX = nextPoint.x * scaleX
                    val nextScaledY = nextPoint.y * scaleY

                    drawLine(
                        color = Color.Yellow,
                        start = Offset(scaledX, scaledY),
                        end = Offset(nextScaledX, nextScaledY),
                        strokeWidth = 2f / scale
                    )
                }
            }
        }
    }
}
