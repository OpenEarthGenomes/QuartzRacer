package com.example.quartzracer.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.quartzracer.R

class QuartzAudioManager(context: Context) {
    private val soundPool: SoundPool
    private val soundMap = mutableMapOf<String, Int>()

    init {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(attributes)
            .build()

        soundMap["boost"] = soundPool.load(context, R.raw.sfx_boost, 1)
        soundMap["brake"] = soundPool.load(context, R.raw.sfx_brake, 1)
        soundMap["hit"] = soundPool.load(context, R.raw.sfx_collision, 1)
        soundMap["collect"] = soundPool.load(context, R.raw.sfx_jackpot, 1)
    }

    fun playBoost() {
        soundMap["boost"]?.let { 
            soundPool.play(it, 1.0f, 1.0f, 0, 0, 1.0f) 
        }
    }

    fun playBrake() {
        soundMap["brake"]?.let { 
            soundPool.play(it, 0.7f, 0.7f, 0, 0, 1.0f) 
        }
    }

    fun playCollision() {
        soundMap["hit"]?.let { 
            soundPool.play(it, 1.0f, 1.0f, 1, 0, 1.0f) 
        }
    }

    fun playCollect() {
        soundMap["collect"]?.let { 
            soundPool.play(it, 0.9f, 0.9f, 0, 0, 1.0f) 
        }
    }

    fun release() {
        soundPool.release()
    }
}
