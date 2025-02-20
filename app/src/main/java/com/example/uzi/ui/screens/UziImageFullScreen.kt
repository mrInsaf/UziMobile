package com.example.uzi.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.uzi.R
import com.mrinsaf.core.data.models.basic.SectorPoint
import com.example.uzi.ui.components.canvas.ZoomableCanvasSector

@Composable
fun UziImageFullScreen(
    imageBitmap: ImageBitmap,
    pointsList: List<SectorPoint>,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        ZoomableCanvasSector(
            imageBitmap = imageBitmap,
            pointsList = pointsList,
        )
    }
}

@Preview
@Composable
fun UziImageFullScreenPreview() {
    UziImageFullScreen(
        imageBitmap = ImageBitmap.imageResource(R.drawable.paint),
        pointsList = emptyList()
    )
}