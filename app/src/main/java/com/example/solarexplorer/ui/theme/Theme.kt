package com.example.solarexplorer.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.isSystemInDarkTheme

import com.example.solarexplorer.ui.theme.AppTypography

// -----------------------------
// LIGHT THEME
// -----------------------------
private val LightColors = lightColorScheme(
    primary = SpacePrimary,
    secondary = SpaceAccent,
    background = Color(0xFFF2F5FA),
    surface = Color.White,
    onPrimary = SpaceOnPrimary,
    onBackground = Color(0xFF0A0F1A),
    onSurface = Color(0xFF0A0F1A),
    primaryContainer = SpacePrimaryVariant
)

// -----------------------------
// DARK THEME
// -----------------------------
private val DarkColors = darkColorScheme(
    primary = SpacePrimary,
    secondary = SpaceAccent,
    background = SpaceBackground,
    surface = SpaceSurface,
    onPrimary = SpaceOnPrimary,
    onBackground = SpaceOnBackground,
    onSurface = SpaceOnBackground,
    primaryContainer = SpacePrimaryVariant
)

// -----------------------------
// MAIN THEME WRAPPER
// -----------------------------
@Composable
fun SolarExplorerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        shapes = Shapes(),
        content = content
    )
}

// -----------------------------
// GRADIENT BACKGROUND
// -----------------------------
@Composable
fun SpaceGradientBackground(
    topColor: Color = SpaceBackground,
    bottomColor: Color = SpaceSurface,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(topColor, bottomColor)
                )
            )
    ) {
        content()
    }
}
