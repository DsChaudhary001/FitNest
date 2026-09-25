package com.fitnest.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.fitnest.app.ui.nav.FitNestApp
import com.fitnest.app.ui.theme.FitNestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitNestTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    FitNestApp()
                }
            }
        }
    }
}
