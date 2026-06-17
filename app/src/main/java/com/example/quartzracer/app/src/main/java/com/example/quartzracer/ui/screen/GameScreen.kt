package com.example.quartzracer.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quartzracer.ui.components.GameCanvas
import com.example.quartzracer.ui.components.SpeedometerGauge
import com.example.quartzracer.viewmodel.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val state by viewModel.gameState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Grafikai motor alul
        GameCanvas(
            gameState = state,
            onSteer = { amt -> viewModel.handleSteering(amt * 1.5f) }
        )

        // FELSŐ HUD: Pontszám, Szint és Háttérzene Címe
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

        // BAL ALSÓ SÁV: Boost Indító Gombok
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

        // JOBB ALSÓ SÁV: Sebességmérő és Fék pedál
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

            // Fék gomb érintés-alapú nyomvatartás szimulációval
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                modifier = Modifier.size(90.dp, 50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("FÉK", color = Color.White)
                // A fék logikát a ViewModel gomb-állapota kezeli le élesben
            }
        }

        // GAME OVER MODAL LAP
        if (state.isGameOver) {
            Surface(
                color = Color(0xDD000000),
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("GAME OVER", color = Color.Red, fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("ELÉRT PONTSZÁM: ${state.score}", color = Color.White, fontSize = 20.sp)
                }
            }
        }
    }
}

