package com.example.quartzracer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quartzracer.audio.MusicPlayerManager
import com.example.quartzracer.audio.QuartzAudioManager
import com.example.quartzracer.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel(application: Application) : AndroidViewModel(application) {
    
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState

    private val musicManager = MusicPlayerManager(application)
    private val audioManager = QuartzAudioManager(application)
    private var entityIdCounter = 0L

    init {
        val initParticles = List(45) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 4f + 2f,
                speed = Random.nextFloat() * 0.03f + 0.01f
            )
        }
        _gameState.update { it.copy(particles = initParticles) }
        startGameLoop()
    }

    fun startMusic() {
        viewModelScope.launch {
            musicManager.scanAndPlayRandomMp3()
            musicManager.currentTrackName.collect { name ->
                _gameState.update { it.copy(currentTrackName = name) }
            }
        }
    }

    private fun startGameLoop() {
        viewModelScope.launch {
            while (true) {
                if (!_gameState.value.isGameOver) {
                    updateGameTick()
                }
                delay(16)
            }
        }
    }

    private fun updateGameTick() {
        _gameState.update { current ->
            val movedParticles = current.particles.map { p ->
                val newY = if (p.y + p.speed > 1f) 0f else p.y + p.speed
                p.copy(y = newY)
            }

            var targetSpeed = 145f
            if (current.isBraking) targetSpeed = 35f
            if (current.boostActiveTicks > 0) {
                targetSpeed = if (current.activeBoostType == EntityType.RED_BOOST) 390f else 290f
            }
            
            val newSpeed = current.speedKmh + (targetSpeed - current.speedKmh) * 0.12f
            val ticksLeft = if (current.boostActiveTicks > 0) current.boostActiveTicks - 1 else 0

            val updatedEntities = current.entities.map { e ->
                e.copy(y = e.y + (e.speed * (newSpeed / 100f)))
            }.filter { e -> e.y < 1.1f }

            val finalEntities = updatedEntities.toMutableList()
            if (Random.nextInt(100) < 5 && finalEntities.size < 5) {
                val spawnType = when (Random.nextInt(10)) {
                    in 0..4 -> EntityType.CAR_LEFT_TO_RIGHT
                    in 5..6 -> EntityType.PEDESTRIAN
                    in 7..8 -> EntityType.BLUE_BOOST
                    else -> EntityType.RED_BOOST
                }
                finalEntities.add(
                    GameEntity(
                        id = entityIdCounter++,
                        type = spawnType,
                        x = Random.nextFloat() * 0.75f + 0.12f,
                        y = 0f,
                        speed = 0.012f + Random.nextFloat() * 0.01f
                    )
                )
            }

            var collectedBlue = current.blueBoostCount
            var collectedRed = current.redBoostCount
            var scoreBonus = 0
            var hitObstacle = false

            val filteredEntities = finalEntities.filter { e ->
                val isCloseY = e.y > 0.78f && e.y < 0.86f
                val isCloseX = Math.abs(e.x - current.playerX) < 0.13f
                
                if (isCloseY && isCloseX) {
                    when (e.type) {
                        EntityType.BLUE_BOOST -> {
                            collectedBlue++
                            audioManager.playCollect()
                            false
                        }
                        EntityType.RED_BOOST -> {
                            collectedRed++
                            audioManager.playCollect()
                            false
                        }
                        else -> {
                            hitObstacle = true
                            false
                        }
                    }
                } else {
                    if (e.y > 0.85f && !isCloseX) scoreBonus += 15
                    true
                }
            }

            if (hitObstacle && current.boostActiveTicks == 0) {
                audioManager.playCollision()
            }

            val nextLevel = (current.score / 3000) + 1

            current.copy(
                particles = movedParticles,
                entities = filteredEntities,
                speedKmh = newSpeed,
                boostActiveTicks = ticksLeft,
                blueBoostCount = collectedBlue,
                redBoostCount = collectedRed,
                level = nextLevel,
                score = current.score + scoreBonus + (newSpeed / 60f).toInt(),
                isGameOver = if (hitObstacle && current.boostActiveTicks == 0) true else current.isGameOver,
                screenShakeIntensity = if (hitObstacle) 35f else Math.max(0f, current.screenShakeIntensity - 2.5f)
            )
        }
    }

    fun handleSteering(amount: Float) {
        _gameState.update { current ->
            val nextX = (current.playerX + amount).coerceIn(0.12f, 0.88f)
            current.copy(playerX = nextX)
        }
    }

    fun setBraking(braking: Boolean) {
        _gameState.update { it.copy(isBraking = braking) }
        if (braking) audioManager.playBrake()
    }

    fun useBoost(isRed: Boolean) {
        _gameState.update { current ->
            if (isRed && current.redBoostCount > 0 && current.boostActiveTicks == 0) {
                audioManager.playBoost()
                current.copy(
                    redBoostCount = current.redBoostCount - 1,
                    boostActiveTicks = 240,
                    activeBoostType = EntityType.RED_BOOST
                )
            } else if (!isRed && current.blueBoostCount > 0 && current.boostActiveTicks == 0) {
                audioManager.playBoost()
                current.copy(
                    blueBoostCount = current.blueBoostCount - 1,
                    boostActiveTicks = 130,
                    activeBoostType = EntityType.BLUE_BOOST
                )
            } else current
        }
    }

    fun toggleGaugeStyle() {
        _gameState.update { current ->
            val nextStyle = when (current.gaugeStyle) {
                GaugeStyle.VEGAS_NEON -> GaugeStyle.RETRO_LCD
                GaugeStyle.RETRO_LCD -> GaugeStyle.TRACK_MODE
                GaugeStyle.TRACK_MODE -> GaugeStyle.VEGAS_NEON
            }
            current.copy(gaugeStyle = nextStyle)
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicManager.stop()
        audioManager.release()
    }
}
