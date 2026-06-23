package ru.practicum.shoppinglist.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val LightColors = lightColorScheme(
    primary = MaterialLightPrimary,
    onPrimary = MaterialLightOnPrimary,
    primaryContainer = MaterialLightPrimaryContainer,
    onPrimaryContainer = MaterialLightOnPrimaryContainer,

    secondary = MaterialLightPrimary,
    secondaryContainer = MaterialLightSecondaryContainer,
    onSecondaryContainer = MaterialLightOnSecondaryContainer,
    secondaryFixed = MaterialLightPrimary,

    background = SchemesLightBackground,

    surface = MaterialLightSurface,
    onSurface = MaterialLightOnSurface,
    onSurfaceVariant = MaterialLightOnSurfaceVariant,
    inverseOnSurface = MaterialLightOnSurface,
    surfaceContainer = MaterialLightSurfaceContainer,
    surfaceContainerLow = MaterialLightSurfaceContainerLow,
    surfaceContainerLowest = MaterialLightSurfaceContainerLowest,
    surfaceContainerHigh = MaterialLightSurfaceContainerHigh,
    surfaceContainerHighest = MaterialLightSurfaceContainerLow,
    surfaceTint = MaterialLightOnSurface12,

    outline = MaterialLightOutline,
    outlineVariant = SchemesLightOutlineVariant,

    scrim = MaterialLightScrim,

    onTertiary = MaterialLightOnSurfaceVariant
)
private val DarkColors = darkColorScheme(
    primary = MaterialDarkMediumContrastPrimary,
    onPrimary = MaterialDarkMediumContrastOnPrimary,
    primaryContainer = MaterialDarkMediumContrastPrimaryContainer,
    onPrimaryContainer = MaterialDarkMediumContrastOnPrimaryContainer,

    secondary = MaterialDarkSecondary,
    secondaryContainer = MaterialDarkMediumContrastSecondaryContainer,
    onSecondaryContainer = MaterialDarkMediumContrastOnSecondaryContainer,
    secondaryFixed = MaterialDarkMediumContrastSecondary,

    background = SchemesDarkBackground,

    surface = MaterialDarkMediumContrastSurface,
    onSurface = MaterialDarkOnSurface,
    onSurfaceVariant = MaterialDarkOnSurfaceVariant,
    inverseOnSurface = MaterialDarkMediumContrastOnSurface,
    surfaceContainer = MaterialDarkMediumContrastSurfaceContainer,
    surfaceContainerLow = MaterialDarkMediumContrastSurfaceContainerLow,
    surfaceContainerLowest = MaterialDarkSurfaceContainerLowest,
    surfaceContainerHigh = MaterialDarkSurfaceContainerHigh,
    surfaceContainerHighest = MaterialDarkSurfaceContainerHighest,
    surfaceTint = MaterialDarkMediumContrastOnSurface12,

    outline = MaterialDarkMediumContrastOutline,
    outlineVariant = SchemesDarkOutlineVariant,

    scrim = MaterialDarkMediumContrastScrim,

    onTertiary = MaterialDarkMediumContrastOnSurfaceVariant
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content,
    )
}
