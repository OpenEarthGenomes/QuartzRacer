package com.example.quartzracer.model

enum class EntityType {
    CAR_LEFT_TO_RIGHT,
    CAR_RIGHT_TO_LEFT,
    PEDESTRIAN,
    BLUE_BOOST,
    RED_BOOST
}

enum class GaugeStyle {
    VEGAS_NEON,
    RETRO_LCD,
    TRACK_MODE
}

data class GameEntity(
    val id: Long,
    val type: EntityType,
    val x: Float,
    val y: Float,
    val speed: Float
)

data class Particle(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float
)

data class GameState(
    val playerX: Float = 0.5f,
    val entities: List<GameEntity> = emptyList(),
    val particles: List<Particle> = emptyList(),
    val score: Int = 0,
    val level: Int = 1,
    val speedKmh: Float = 140f,
    val isGameOver: Boolean = false,
    val blueBoostCount: Int = 0,
    val redBoostCount: Int = 0,
    val boostActiveTicks: Int = 0,
    val activeBoostType: EntityType? = null,
    val currentTrackName: String? = "Média keresése...",
    val gaugeStyle: GaugeStyle = GaugeStyle.VEGAS_NEON,
    val isBraking: Boolean = false,
    val screenShakeIntensity: Float = 0f
)
