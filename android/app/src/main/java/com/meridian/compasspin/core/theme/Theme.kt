package com.meridian.compasspin.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AtlasColorScheme = lightColorScheme(
    primary = Land,
    onPrimary = Paper,
    primaryContainer = Land,
    onPrimaryContainer = Paper,
    secondary = Accent,
    onSecondary = Paper,
    secondaryContainer = AccentLight,
    onSecondaryContainer = Paper,
    tertiary = LandStroke,
    onTertiary = Paper,
    background = Paper,
    onBackground = InkText,
    surface = PaperSurface,
    onSurface = InkText,
    surfaceVariant = PaperSurface,
    onSurfaceVariant = Land,
    surfaceContainerHighest = PaperRaised,
    outline = Hairline,
    outlineVariant = Hairline,
    error = Accent,
    onError = Paper,
    scrim = DarkGroundTop
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AtlasColorScheme,
        typography = AtlasTypography,
        content = content
    )
}
