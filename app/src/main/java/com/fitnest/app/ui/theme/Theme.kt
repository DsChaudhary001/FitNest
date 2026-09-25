package com.fitnest.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val FitNestGreen = Color(0xFF2E7D5B)
val FitNestGreenLight = Color(0xFF6FBF9A)
val FitNestBackground = Color(0xFFF7F9F8)
val FitNestSurface = Color(0xFFFFFFFF)
val FitNestAccent = Color(0xFFFF8A5B)

private val LightColors = lightColorScheme(
    primary = FitNestGreen,
    secondary = FitNestAccent,
    background = FitNestBackground,
    surface = FitNestSurface
)

private val DarkColors = darkColorScheme(
    primary = FitNestGreenLight,
    secondary = FitNestAccent
)

@Composable
fun FitNestTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(colorScheme = colors, content = content)
}
