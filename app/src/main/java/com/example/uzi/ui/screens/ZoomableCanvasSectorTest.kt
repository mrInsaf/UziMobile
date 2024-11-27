package com.example.uzi.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import com.example.uzi.R
import com.example.uzi.data.models.SectorPoint
import com.example.uzi.ui.components.canvas.ZoomableCanvasSectorWithConstraints

@Composable
fun PreviewZoomableCanvasSector() {
    val points = listOf(
        SectorPoint(100, 100),
        SectorPoint(200, 100),
        SectorPoint(200, 200),
        SectorPoint(100, 200)
    )
    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.nice_peizaj)

    Column(
//        modifier = Modifier.fillMaxSize()
    ) {
        ZoomableCanvasSectorWithConstraints(
            imageBitmap = imageBitmap, pointsList = points,
            onFullScreen = { TODO() }
        )
    }

}
