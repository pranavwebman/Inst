package com.instaeducate.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = BlueLightPrimary,
    secondary = BlueLightSecondary,
    tertiary = BlueLightTertiary,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = LightSurface,
    onSecondary = LightSurface,
    onBackground = LightTextPrimary,
    onSurface = LightTextPrimary
)

private val DarkColorScheme = darkColorScheme(
    primary = BlueDarkPrimary,
    secondary = BlueDarkSecondary,
    tertiary = BlueDarkTertiary,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = AmoledBackground,
    onSecondary = AmoledBackground,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary
)

private val AmoledColorScheme = darkColorScheme(
    primary = BlueDarkPrimary,
    secondary = BlueDarkSecondary,
    tertiary = BlueDarkTertiary,
    background = AmoledBackground,
    surface = AmoledSurface,
    onPrimary = AmoledBackground,
    onSecondary = AmoledBackground,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary
)

@Composable
fun InstaEducateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    amoledMode: Boolean = true, // Premium feature enabled by default
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        amoledMode && darkTheme -> AmoledColorScheme
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            
            // Set status/nav bar text colors based on mode
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
