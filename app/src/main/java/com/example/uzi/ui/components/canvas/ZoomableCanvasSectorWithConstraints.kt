package com.example.uzi.ui.components.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.uzi.data.models.SectorPoint

@Composable
fun ZoomableCanvasSectorWithConstraints(
    imageBitmap: ImageBitmap,
    pointsList: List<SectorPoint>,
    onFullScreen: () -> Unit // Новый параметр
) {
    ZoomableCanvasSector(
        imageBitmap = imageBitmap,
        pointsList = pointsList,
        modifier = Modifier
            .clip(shape = RoundedCornerShape(20.dp))
            .clipToBounds() // Гарантируем обрезку содержимого за пределами рамок
            .clickable { onFullScreen() }
    )
}
