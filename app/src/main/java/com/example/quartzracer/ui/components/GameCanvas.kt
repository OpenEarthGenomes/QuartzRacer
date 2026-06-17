package com.example.quartzracer.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.quartzracer.model.GameState

@Composable
fun GameCanvas(gameState: GameState, onInput: (Float) -> Unit) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // 3D Autó renderelő logika, 
        // Forgalom és palackok kirajzolása
    }
}

