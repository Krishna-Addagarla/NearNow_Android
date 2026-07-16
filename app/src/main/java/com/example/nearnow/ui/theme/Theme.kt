package com.example.nearnow.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NearNowDarkColorScheme = darkColorScheme(
    primary = Signal,
    onPrimary = Ink,

    secondary = Coral,
    onSecondary = Paper,

    tertiary = Signal,

    background = Ink,
    onBackground = Paper,

    surface = Color(0xFF121A2B),
    onSurface = Paper,

    surfaceVariant = Color(0xFF1A2438),
    onSurfaceVariant = Slate,

    outline = Color(0xFF2B3447)
)

private val NearNowLightColorScheme = lightColorScheme(
    primary = Signal,
    onPrimary = Ink,

    secondary = Coral,
    onSecondary = Paper,

    tertiary = Signal,

    background = Color(0xFFF8F9FC),
    onBackground = Ink,

    surface = Color.White,
    onSurface = Ink,

    surfaceVariant = Color(0xFFF0F3F8),
    onSurfaceVariant = Slate,

    outline = Color(0xFFD7DEE8)
)

@Composable
fun NearNowTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme)
            NearNowDarkColorScheme
        else
            NearNowLightColorScheme,
        typography = Typography,
        content = content
    )
}