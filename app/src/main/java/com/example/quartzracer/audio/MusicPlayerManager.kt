package com.example.quartzracer.audio

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.flow.MutableStateFlow

class MusicPlayerManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    val currentTrackName = MutableStateFlow<String?>(null)

    fun initializeAndPlay() {
        // Logika a MediaStore lekérdezéséhez (ahogy korábban írtuk)
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

