package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ElegantDarkColorScheme = darkColorScheme(
    primary = DarkPrimary,                // #D0E4FF
    onPrimary = DarkOnPrimary,            // #003258
    primaryContainer = DarkPrimaryContainer, // #00497D
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,            // #D6BEE4
    onSecondary = DarkOnSecondary,        // #3B2948
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkElevatedSurface,       // #43474E
    onTertiary = DarkOnBackground,
    tertiaryContainer = DarkElevatedSurface,
    background = DarkBackground,          // #1A1C1E
    onBackground = DarkOnBackground,      // #E2E2E6
    surface = DarkBackground,             // #1A1C1E
    onSurface = DarkOnBackground,         // #E2E2E6
    surfaceVariant = DarkSurfaceVariant,  // #2E3033
    onSurfaceVariant = DarkOnSurfaceVariant, // #C4C6CF
    surfaceContainerLowest = Color(0xFF222427),
    surfaceContainerLow = DarkSurfaceVariant, // #2E3033
    surfaceContainer = DarkSurfaceVariant,    // #2E3033
    surfaceContainerHigh = DarkElevatedSurface, // #43474E
    surfaceContainerHighest = Color(0xFF4F535A),
    outline = DarkBorder,                 // #43474E
    outlineVariant = DarkBorderActive     // #5A5E66
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = ElegantDarkColorScheme,
        typography = Typography,
        content = content
    )
}
