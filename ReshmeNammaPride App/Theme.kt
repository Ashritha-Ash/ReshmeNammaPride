package com.example.reshmenammapride.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PrimaryGreen = Color(0xFF2E7D32)
val SecondaryGold = Color(0xFFC8A951)
val BackgroundCream = Color(0xFFFAF9F6)
val SuccessGreen = Color(0xFF388E3C)
val WarningOrange = Color(0xFFF57C00)
val DangerRed = Color(0xFFD32F2F)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    primaryContainer = Color(0xFFA5D6A7), // Light green container
    onPrimaryContainer = Color(0xFF002107),
    secondary = SecondaryGold,
    secondaryContainer = Color(0xFFEFE0A1),
    onSecondaryContainer = Color(0xFF221B00),
    tertiary = SuccessGreen,
    background = BackgroundCream,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    error = DangerRed
)

@Composable
fun ReshmeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}