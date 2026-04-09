package com.example.smartbudget.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Dark mode color scheme with primary colors
private val DarkColorScheme = darkColorScheme(
    primary = skyBlue,
    onPrimary = Color.Black,
    primaryContainer = indigoPrimary,
    onPrimaryContainer = Color.White,

    secondary = blueAccent,
    onSecondary = Color.Black,
    secondaryContainer = indigoPrimary.copy(alpha = 0.7f),
    onSecondaryContainer = Color.White,

    tertiary = skyBlue.copy(alpha = 0.85f),
    onTertiary = Color.Black,
    tertiaryContainer = blueAccent.copy(alpha = 0.6f),
    onTertiaryContainer = Color.White,

    background = Color(0xFF0F0F0F),
    onBackground = Color(0xFFE8E8E8),
    surface = Color(0xFF1A1A1A),
    onSurface = Color(0xFFE8E8E8),
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFB0B0B0),

    error = Color(0xFFCF6679),
    onError = Color.Black,
    errorContainer = Color(0xFFB00020),
    onErrorContainer = Color.White
)

// Light mode color scheme with primary colors
private val LightColorScheme = lightColorScheme(
    primary = indigoPrimary,
    onPrimary = Color.White,
    primaryContainer = indigoPrimary.copy(alpha = 0.15f),
    onPrimaryContainer = indigoPrimary,

    secondary = blueAccent,
    onSecondary = Color.White,
    secondaryContainer = blueAccent.copy(alpha = 0.15f),
    onSecondaryContainer = blueAccent,

    tertiary = skyBlue,
    onTertiary = Color.Black,
    tertiaryContainer = skyBlue.copy(alpha = 0.15f),
    onTertiaryContainer = skyBlue,

    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1A1A1A),
    surface = Color.White,
    onSurface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFFF0F0F0),
    onSurfaceVariant = Color(0xFF6B6B6B),

    error = Color(0xFFB00020),
    onError = Color.White,
    errorContainer = Color(0xFFCF6679),
    onErrorContainer = Color.Black
)

@Composable
fun SmartBudgetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}