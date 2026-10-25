package com.app.kind.kind.prime.kindprimeapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

/**
 * La paleta de KindPrime está pensada para un tema oscuro, por lo que se aplica
 * siempre, independientemente del tema claro/oscuro del sistema.
 */
private val KindPrimeColorScheme = darkColorScheme(
    primary = KindPrimary,
    onPrimary = KindOnPrimary,
    primaryContainer = KindPrimary,
    onPrimaryContainer = KindOnPrimary,
    inversePrimary = KindPrimary,

    secondary = KindTextSecondary,
    onSecondary = KindBackground,
    secondaryContainer = KindPrimary.copy(alpha = 0.16f),
    onSecondaryContainer = KindPrimary,

    tertiary = KindWarning,
    onTertiary = KindBackground,
    tertiaryContainer = KindSurface,
    onTertiaryContainer = KindWarning,

    background = KindBackground,
    onBackground = KindTextPrimary,
    surface = KindBackground,
    onSurface = KindTextPrimary,
    surfaceVariant = KindSurface,
    onSurfaceVariant = KindTextSecondary,
    surfaceTint = KindSurface,
    inverseSurface = KindTextPrimary,
    inverseOnSurface = KindBackground,

    error = KindError,
    onError = KindBackground,
    errorContainer = KindSurface,
    onErrorContainer = KindError,

    outline = KindTextSecondary,
    outlineVariant = KindSurface,
    scrim = KindBackground,

    surfaceBright = KindSurface,
    surfaceDim = KindBackground,
    surfaceContainerLowest = KindBackground,
    surfaceContainerLow = KindSurface,
    surfaceContainer = KindSurface,
    surfaceContainerHigh = KindSurface,
    surfaceContainerHighest = KindSurface,
)

@Composable
fun KindPrimeAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KindPrimeColorScheme,
        typography = Typography,
        content = content,
    )
}
