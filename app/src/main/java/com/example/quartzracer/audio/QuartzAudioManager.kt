package com.example.quartzracer.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.openearthgenomes.quartzracer.R

class QuartzAudioManager(private val context: Context) {
    private var soundPool: SoundPool? = null
    private var beepLowId: Int = 0
    private var beepHighId: Int = 0
    private var boostId: Int = 0
    private var brakeId: Int = 0
    private var collisionId: Int = 0
    private var casinoJackpotId: Int = 0

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        soundPool?.let { pool ->
            beepLowId = pool.load(context, R.raw.beep_low, 1)
            beepHighId = pool.load(context, R.raw.beep_high, 1)
            boostId = pool.load(context, R.raw.boost, 1)
            brakeId = pool.load(context, R.raw.brake, 1)
            collisionId = pool.load(context, R.raw.collision, 1)
            casinoJackpotId = pool.load(context, R.raw.casino_jackpot, 1)
        }
    }

    fun playBeepLow(rate: Float = 1.0f) {
        soundPool?.play(beepLowId, 1.0f, 1.0f, 1, 0, rate)
    }

    fun playBeepHigh(rate: Float = 1.0f) {
        soundPool?.play(beepHighId, 1.0f, 1.0f, 1, 0, rate)
    }

    fun playBoost() {
        soundPool?.play(boostId, 1.0f, 1.0f, 2, 0, 1.0f)
    }

    fun playBrake() {
        soundPool?.play(brakeId, 0.8f, 0.8f, 2, 0, 1.0f)
    }

    fun playCollision() {
        soundPool?.play(collisionId, 1.0f, 1.0f, 3, 0, 1.0f)
    }

    fun playCasinoJackpot() {
        soundPool?.play(casinoJackpotId, 1.0f, 1.0f, 4, 0, 1.0f)
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }
}

