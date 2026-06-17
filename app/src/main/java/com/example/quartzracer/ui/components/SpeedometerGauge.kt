package com.example.quartzracer.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.quartzracer.model.GaugeStyle
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SpeedometerGauge(
    speed: Float,
    style: GaugeStyle,
    modifier: Modifier = Modifier,
    maxSpeed: Float = 390f
) {
    val startAngle = 150f
    val sweepAngle = 240f
    val progress = (speed / maxSpeed).coerceIn(0f, 1f)
    val needleAngle = startAngle + (progress * sweepAngle)

    Canvas(modifier = modifier.size(125.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val r = size.width / 2 - 6f

        when (style) {
            GaugeStyle.VEGAS_NEON -> {
                drawCircle(Color(0xDD030508), radius = r, center = center)
                drawArc(Color.DarkGray, startAngle, sweepAngle, false, Stroke(5f))
                drawArc(Color(0xFF00E5FF), startAngle, progress * sweepAngle, false, Stroke(5f, cap = StrokeCap.Round))
                
                val rad = Math.toRadians(needleAngle.toDouble())
                val nX = center.x + (r - 12f) * cos(rad).toFloat()
                val nY = center.y + (r - 12f) * sin(rad).toFloat()
                drawLine(Color(0xFFFF00E4), center, Offset(nX, nY), strokeWidth = 5f, cap = StrokeCap.Round)
            }
            GaugeStyle.RETRO_LCD -> {
                drawRect(Color(0xFF1A1910), Offset.Zero, size)
                drawRect(Color(0xFFFFB000), Offset(3f, 3f), Size(size.width - 6f, size.height - 6f), style = Stroke(2.5f))
                
                val rad = Math.toRadians(needleAngle.toDouble())
                val nX = center.x + (r - 8f) * cos(rad).toFloat()
                val nY = center.y + (r - 8f) * sin(rad).toFloat()
                drawLine(Color(0xFFFFB000), center, Offset(nX, nY), strokeWidth = 7f)
            }
            GaugeStyle.TRACK_MODE -> {
                drawCircle(Color(0xFF0A0A0A), radius = r, center = center)
                drawArc(Color.Red, startAngle, progress * sweepAngle, false, Stroke(14f))
            }
        }
    }
}
