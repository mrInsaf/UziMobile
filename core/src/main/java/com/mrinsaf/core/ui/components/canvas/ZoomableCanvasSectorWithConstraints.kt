package com.mrinsaf.core.ui.components.canvas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.mrinsaf.core.data.models.basic.SectorPoint

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
