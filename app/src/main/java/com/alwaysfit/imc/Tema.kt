package com.alwaysfit.imc

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Esquema = darkColorScheme(
    primary = Color.White,
    onPrimary = Color(0xFF0B7F8C),
    background = Color(0xFF0B7F8C),
    surface = Color(0xFF0B7F8C),
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun AlwaysFitTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Esquema, content = content)
}
