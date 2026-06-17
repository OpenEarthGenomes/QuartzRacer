package com.example.quartzracer.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quartzracer.GameViewModel
import com.example.quartzracer.ui.components.SpeedometerGauge

@Composable
fun GameScreen(viewModel: GameViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // HUD kijelzők
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "SCORE: ${viewModel.score}", style = MaterialTheme.typography.headlineMedium)
            Text(text = "JACKPOT: ${viewModel.jackpotMultiplier}x", style = MaterialTheme.typography.headlineMedium)
        }

        // Sebességmérő óra
        SpeedometerGauge(speed = viewModel.speed)

        // Game Over állapot tiszta kezelése
        if (viewModel.isGameOver) {
            Surface(
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.errorContainer
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "GAME OVER", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.resetGame() }) {
                        Text(text = "RESTART")
                    }
                }
            }
        }

        // Irányító gombok az alján
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { viewModel.activateBoost() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "BOOST")
            }

            Button(
                onClick = { viewModel.applyBrake() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(text = "BRAKE")
            }
        }
    }
}
