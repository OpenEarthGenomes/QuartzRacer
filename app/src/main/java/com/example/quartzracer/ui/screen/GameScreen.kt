package com.example.quartzracer.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quartzracer.ui.components.GameCanvas
import com.example.quartzracer.ui.components.SpeedometerGauge
import com.example.quartzracer.viewmodel.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val state by viewModel.gameState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        GameCanvas(
            gameState = state,
            onSteer = { amt -> viewModel.handleSteering(amt * 1.6f) }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("SCORE: ${state.score}", color = Color.White, fontSize = 18.sp)
                Text("LEVEL: ${state.level}", color = Color.White, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "TRACK: ${state.currentTrackName ?: "Keresés..."}",
                color = Color(0xFF00E5FF),
                fontSize = 12.sp
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Button(
                onClick = { viewModel.useBoost(isRed = true) },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF0055)),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text("RED BOOST (${state.redBoostCount})", color = Color.White)
            }
            Button(
                onClick = { viewModel.useBoost(isRed = false) },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00E5FF))
            ) {
                Text("BLUE BOOST (${state.blueBoostCount})", color = Color.Black)
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpeedometerGauge(
                speed = state.speedKmh,
                style = state.gaugeStyle,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { viewModel.toggleGaugeStyle() }
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = if (state.isBraking) Color.Red else Color.DarkGray,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .size(90.dp, 50.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                viewModel.setBraking(true)
                                tryAwaitRelease()
                                viewModel.setBraking(false)
                            }
                        )
                    }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("FÉK", color = Color.White, fontSize = 16.sp)
                }
            }
        }

        if (state.isGameOver) {
            Surface(
                color = Color(0xEE000000),
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("GAME OVER", color = Color.Red, fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("ELÉRT PONTSZÁM: ${state.score}", color = Color.White, fontSize = 20.sp)
                }
            }
        }
    }
}
