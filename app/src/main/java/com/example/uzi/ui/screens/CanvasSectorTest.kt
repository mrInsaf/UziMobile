package com.example.uzi.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.uzi.data.mock.mockSectorPointsList
import com.example.uzi.ui.components.CanvasSector

@Composable
fun CanvasSectorTest() {
    CanvasSector(
        pointsList = mockSectorPointsList
    )
}

@Preview
@Composable
fun CanvasSectorTestPreview() {
    CanvasSectorTest()
}