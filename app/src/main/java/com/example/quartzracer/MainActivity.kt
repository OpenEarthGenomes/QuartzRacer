package com.example.quartzracer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewmodel.compose.viewModel
import androidx.core.content.ContextCompat
import com.example.quartzracer.ui.screen.GameScreen
import com.example.quartzracer.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startTrackingAndMusic()
        }
    }

    private var globalViewModel: GameViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val gameViewModel: GameViewModel = viewModel()
            globalViewModel = gameViewModel
            GameScreen(viewModel = gameViewModel)
        }

        checkAudioPermissions()
    }

    private fun checkAudioPermissions() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) == 
            PackageManager.PERMISSION_GRANTED) {
            startTrackingAndMusic()
        } else {
            requestPermissionLauncher.launch(permission)
        }
    }

    private fun startTrackingAndMusic() {
        globalViewModel?.startMusic()
    }
}
