package com.example.pokedexapp.theme

// Import colors from color.kt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

// Main Light Color Scheme
val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = OnPrimaryColor,
    surface = SurfaceColor,
    onSurface = OnSurfaceColor,
    surfaceVariant = SurfaceVariantColor,
    background = BackgroundColor,
    onBackground = OnBackgroundColor,
    surfaceDim = SurfaceDimColor,
)


// Apply the light theme with custom Pokémon colors
@Composable
fun PokedexAppTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalCustomColors provides CustomColors()
    ) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = Typography,
            content = content
        )
    }
}