package com.example.quartzracer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.quartzracer.ui.screen.GameScreen
import com.example.quartzracer.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Hagyományos ViewModel példányosítás, amihez nem kell a compose-viewmodel kiegészítő
        val gameViewModel = ViewModelProvider(this)[GameViewModel::class.java]
        
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                GameScreen(viewModel = gameViewModel)
            }
        }
    }
}
