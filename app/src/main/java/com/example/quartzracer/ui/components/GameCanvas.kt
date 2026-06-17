package com.example.quartzracer.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import com.example.quartzracer.model.EntityType
import com.example.quartzracer.model.GameState

@Composable
fun GameCanvas(
    gameState: GameState,
    onSteer: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val time = System.currentTimeMillis()

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF06080B))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onSteer(dragAmount.x / size.width)
                }
            }
    ) {
        val w = size.width
        val h = size.height
        val horizonY = h * 0.36f

        val shakeX = (Math.random().toFloat() - 0.5f) * gameState.screenShakeIntensity
        val shakeY = (Math.random().toFloat() - 0.5f) * gameState.screenShakeIntensity

        withTransform({
            translate(left = shakeX, top = shakeY)
        }) {
            drawRect(Color(0xFF010204), Offset(0f, 0f), Size(w, horizonY))
            
            val neonColor = if ((time / 250) % 2 == 0L) Color(0xFFFF00D4) else Color(0xFF00E5FF)
            drawRect(Color(0xFF0F121A), Offset(w * 0.12f, horizonY - 120f), Size(65f, 120f))
            drawRect(neonColor, Offset(w * 0.12f + 4f, horizonY - 120f), Size(4f, 120f))
            drawRect(Color(0xFF141822), Offset(w * 0.65f, horizonY - 150f), Size(85f, 150f))

            val roadPath = Path().apply {
                moveTo(w * 0.42f, horizonY)
                lineTo(w * 0.58f, horizonY)
                lineTo(w * 0.96f, h)
                lineTo(w * 0.04f, h)
                close()
            }
            drawPath(path = roadPath, color = Color(0xFF0F1218))

            val stepPhase = (time * (gameState.speedKmh / 28f)).toInt() % 180
            for (yPos in horizonY.toInt()..h.toInt() step 35) {
                val animY = ((yPos + stepPhase - horizonY.toInt()) % (h - horizonY)) + horizonY
                val ratio = (animY - horizonY) / (h - horizonY)
                val currentRoadWidth = w * 0.16f + ratio * (w * 0.92f - w * 0.16f)
                val centerX = w * 0.5f
                
                drawCircle(Color(0xFFFFEE00), 3.5f + (ratio * 5.5f), Offset(centerX - currentRoadWidth / 2, animY))
                drawCircle(Color(0xFFFFEE00), 3.5f + (ratio * 5.5f), Offset(centerX + currentRoadWidth / 2, animY))
            }

            gameState.entities.forEach { entity ->
                val eRatio = entity.y
                val currentWidth = w * 0.16f + eRatio * (w * 0.92f - w * 0.16f)
                val eX = (w * 0.5f - currentWidth / 2) + (entity.x * currentWidth)
                val eY = horizonY + (eRatio * (h - horizonY))
                val sizeScale = 14f + (eRatio * 46f)

                when (entity.type) {
                    EntityType.BLUE_BOOST -> {
                        drawRect(Color(0xFF00E5FF), Offset(eX - sizeScale/3, eY - sizeScale), Size(sizeScale * 0.6f, sizeScale))
                    }
                    EntityType.RED_BOOST -> {
                        drawRect(Color(0xFFFF0044), Offset(eX - sizeScale/3, eY - sizeScale), Size(sizeScale * 0.6f, sizeScale))
                    }
                    EntityType.PEDESTRIAN -> {
                        drawCircle(Color(0xFF00E676), sizeScale * 0.28f, Offset(eX, eY - sizeScale))
                        drawLine(Color(0xFF00E676), Offset(eX, eY - sizeScale), Offset(eX, eY), 5f)
                    }
                    else -> {
                        val cColor = if (entity.type == EntityType.CAR_LEFT_TO_RIGHT) Color(0xFFFF5252) else Color(0xFFE040FB)
                        drawRect(cColor, Offset(eX - sizeScale, eY - sizeScale), Size(sizeScale * 2, sizeScale))
                    }
                }
            }

            val pRatio = 0.83f
            val pRoadWidth = w * 0.16f + pRatio * (w * 0.92f - w * 0.16f)
            val playerX = (w * 0.5f - pRoadWidth / 2) + (gameState.playerX * pRoadWidth)
            val playerY = horizonY + (pRatio * (h - horizonY))

            val cW = 72f
            val cL = 95f
            val cH = 30f

            drawOval(Color(0x99000000), Offset(playerX - cW, playerY + 8f), Size(cW * 2, 28f))

            val perspectiveShift = (gameState.playerX - 0.5f) * 32f

            val sidePath = Path().apply {
                if (perspectiveShift > 0) {
                    moveTo(playerX - cW, playerY + cL / 2)
                    lineTo(playerX - cW - perspectiveShift, playerY - cL / 2)
                    lineTo(playerX - cW - perspectiveShift, playerY - cL / 2 - cH)
                    lineTo(playerX - cW, playerY + cL / 2 - cH)
                } else {
                    moveTo(playerX + cW, playerY + cL / 2)
                    lineTo(playerX + cW - perspectiveShift, playerY - cL / 2)
                    lineTo(playerX + cW - perspectiveShift, playerY - cL / 2 - cH)
                    lineTo(playerX + cW, playerY + cL / 2 - cH)
                }
                close()
            }
            drawPath(sidePath, Color(0xFF007A87))

            val backPath = Path().apply {
                moveTo(playerX - cW, playerY + cL / 2)
                lineTo(playerX + cW, playerY + cL / 2)
                lineTo(playerX + cW, playerY + cL / 2 - cH)
                lineTo(playerX - cW, playerY + cL / 2 - cH)
                close()
            }
            drawPath(backPath, Color(0xFF00565B))

            val topPath = Path().apply {
                moveTo(playerX - cW, playerY + cL / 2 - cH)
                lineTo(playerX - cW - perspectiveShift, playerY - cL / 2 - cH)
                lineTo(playerX + cW - perspectiveShift, playerY - cL / 2 - cH)
                lineTo(playerX + cW, playerY + cL / 2 - cH)
                close()
            }
            
            val activeNeonColor = if (gameState.boostActiveTicks > 0) {
                if (gameState.activeBoostType == EntityType.RED_BOOST) Color(0xFFFF0055) else Color(0xFF00E5FF)
            } else {
                Color(0xFF00E5FF)
            }
            drawPath(topPath, activeNeonColor)

            val lightColor = if (gameState.isBraking) Color.Red else Color(0xFFFF1744)
            val lightRadius = if (gameState.isBraking) 15f else 8f
            drawCircle(lightColor, lightRadius, Offset(playerX - cW + 14f, playerY + cL / 2 - cH / 2))
            drawCircle(lightColor, lightRadius, Offset(playerX + cW - 14f, playerY + cL / 2 - cH / 2))

            gameState.particles.forEach { p ->
                val px = p.x * w
                val py = horizonY + (p.y * (h - horizonY))
                val length = p.size * (gameState.speedKmh / 45f)
                drawLine(
                    color = Color(0x66B2EBF2),
                    start = Offset(px, py),
                    end = Offset(px, py + length),
                    strokeWidth = 2.2f
                )
            }
        }
    }
}
