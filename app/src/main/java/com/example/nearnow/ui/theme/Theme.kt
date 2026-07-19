package com.example.nearnow.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NearNowLightColorScheme = lightColorScheme(
    primary = Mango,
    onPrimary = CardWhite,
    secondary = Teal,
    onSecondary = TextPrimary,
    tertiary = Coral,
    background = Cream,
    onBackground = TextPrimary,
    surface = CardWhite,
    onSurface = TextPrimary,
    surfaceVariant = SoftGray,
    onSurfaceVariant = TextSecondary,
    outline = SoftGray
)

private val NearNowDarkColorScheme = darkColorScheme(
    primary = Mango,
    onPrimary = TextPrimary,
    secondary = Teal,
    onSecondary = TextPrimary,
    tertiary = Coral,
    background = Color(0xFF0F172A), // Soft dark surface
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF475569)
)

@Composable
fun NearNowTheme(
    darkTheme: Boolean = false, // Set to false by default for light theme redesign
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) NearNowDarkColorScheme else NearNowLightColorScheme,
        typography = Typography,
        content = content
    )
}