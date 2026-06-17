package com.example.quartzracer.viewmodel

import androidx.lifecycle.ViewModel
import com.example.quartzracer.model.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState

    fun handleInput(dx: Float) {
        // Kormányzás logikája
    }

    fun useBoost(isRed: Boolean) {
        // Boost aktiválás
    }
}

