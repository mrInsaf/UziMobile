package com.example.uzi.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mrinsaf.core.data.mock.mockSectorPointsList
import com.example.uzi.ui.components.canvas.CanvasSector

@Composable
fun CanvasSectorTest() {
    CanvasSector(
        pointsList = com.mrinsaf.core.data.mock.mockSectorPointsList
    )
}

@Preview
@Composable
fun CanvasSectorTestPreview() {
    CanvasSectorTest()
}