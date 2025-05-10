package com.mrinsaf.diagnostic_details.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import com.mrinsaf.core.R
import com.mrinsaf.core.domain.model.basic.SectorPoint
import com.mrinsaf.core.presentation.ui.components.canvas.ZoomableCanvasSector

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