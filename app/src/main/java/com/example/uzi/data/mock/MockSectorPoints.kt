package com.example.uzi.data.mock

import com.example.uzi.data.models.basic.SectorPoint
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


fun generateConvexPoints(centerX: Int, centerY: Int, radius: Int, numPoints: Int): List<SectorPoint> {
    val points = mutableListOf<SectorPoint>()
    val angleStep = 2 * Math.PI / numPoints

    for (i in 0 until numPoints) {
        val angle = i * angleStep
        val x = (centerX + radius * cos(angle)).roundToInt()
        val y = (centerY + radius * sin(angle)).roundToInt()
        points.add(SectorPoint(x, y))
    }

    return points
}

val mockSectorPointsList = generateConvexPoints(
    centerX = 100,
    centerY = 100,
    radius = 100,
    numPoints = 6
)
