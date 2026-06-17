package com.example.quartzracer.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SpeedometerGauge(
    speed: Float,
    maxSpeed: Float = 180f,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(200.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.width / 2
        
        // Háttér ív
        drawArc(
            color = Color.Gray.copy(alpha = 0.3f),
            startAngle = 150f,
            sweepAngle = 240f,
            useCenter = false,
            topLeft = Offset.Zero,
            size = size,
            style = Stroke(width = 12f)
        )

        // Aktuális sebesség ív
        val sweepAngle = (speed / maxSpeed) * 240f
        drawArc(
            color = Color.Green,
            startAngle = 150f,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset.Zero,
            size = size,
            style = Stroke(width = 12f)
        )

        // Mutató kiszámítása retro kvarc stílusban
        val angleRad = Math.toRadians((150f + sweepAngle).toDouble())
        val needleLength = radius * 0.8f
        val endPoint = Offset(
            x = center.x + (needleLength * cos(angleRad)).toFloat(),
            y = center.y + (needleLength * sin(angleRad)).toFloat()
        )

        drawLine(
            color = Color.Red,
            start = center,
            end = endPoint,
            strokeWidth = 5f
        )
    }
}
