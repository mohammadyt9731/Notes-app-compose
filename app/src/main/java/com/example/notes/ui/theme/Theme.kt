package com.example.notes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primaryVariant = Purple700,
    secondary = Teal200,

    background = SemiWhite,
    onBackground = Black,
    primary = Yellow,
    onPrimary = DarkGray,
    surface = LightGray,
    onSurface = Gray
)

private val LightColorPalette = lightColors(

    primaryVariant = Purple700,
    secondary = Teal200,

    background = White,
    onBackground = Black,
    primary = Yellow,
    onPrimary = DarkGray,
    surface = LightGray,
    onSurface = Gray
)

@Composable
fun NotesTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}