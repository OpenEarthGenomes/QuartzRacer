package com.example.quartzracer.audio

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.flow.MutableStateFlow

class MusicPlayerManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    val currentTrackName = MutableStateFlow("Nincs zene")

    fun scanAndPlayRandomMp3() {
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val cursor = context.contentResolver.query(
            uri, projection, selection, null, null
        )

        if (cursor != null && cursor.moveToFirst()) {
            val idColumn = cursor.getColumnIndexOrThrow(
                MediaStore.Audio.Media._ID
            )
            val titleColumn = cursor.getColumnIndexOrThrow(
                MediaStore.Audio.Media.TITLE
            )

            val randomIndex = (0 until cursor.count).random()
            cursor.moveToPosition(randomIndex)

            val id = cursor.getLong(idColumn)
            val title = cursor.getString(titleColumn)
            currentTrackName.value = title

            val contentUri = Uri.withAppendedPath(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                id.toString()
            )

            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, contentUri).apply {
                isLooping = true
                start()
            }
        } else {
            currentTrackName.value = "Nincs helyi MP3 fájl"
        }
        cursor?.close()
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
