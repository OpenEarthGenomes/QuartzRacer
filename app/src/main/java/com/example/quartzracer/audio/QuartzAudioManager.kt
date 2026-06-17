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
            .setMaxStreams(5)
            .setAudioAttributes(attributes)
            .build()

        // Itt töltjük be a res/raw mappában lévő hangfájlokat
        // Cseréld ki a fájlneveket a sajátjaidra!
        soundMap["boost"] = soundPool.load(context, R.raw.sfx_boost, 1)
        soundMap["brake"] = soundPool.load(context, R.raw.sfx_brake, 1)
        soundMap["collision"] = soundPool.load(context, R.raw.sfx_collision, 1)
        soundMap["jackpot"] = soundPool.load(context, R.raw.sfx_jackpot, 1)
    }

    fun playBoost() {
        soundMap["boost"]?.let { soundPool.play(it, 1f, 1f, 0, 0, 1f) }
    }

    fun playBrake() {
        soundMap["brake"]?.let { soundPool.play(it, 0.8f, 0.8f, 0, 0, 1f) }
    }

    fun playCollision() {
        soundMap["collision"]?.let { soundPool.play(it, 1f, 1f, 0, 0, 1f) }
    }

    fun playJackpot() {
        soundMap["jackpot"]?.let { soundPool.play(it, 1f, 1f, 0, 0, 1f) }
    }

    fun release() {
        soundPool.release()
    }
}

