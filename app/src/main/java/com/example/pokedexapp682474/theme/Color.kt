package com.example.pokedexapp682474.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val PrimaryColor = Color(0xFF5631E8)
val OnPrimaryColor = Color(0xFFFFFFFF)
val SurfaceColor = Color(0xFFEDF6FF)
val OnSurfaceColor = Color(0xFF0E0940)
val SurfaceVariantColor = Color(0xFFF7F7FF)
val SurfaceDimColor = Color(0x650E0940)
val BackgroundColor = Color(0xFFE0F7FA)
val OnBackgroundColor = Color(0xFF000000)

private val GrassGreen = Color(0xFF2AF9C9)
private val FireRed = Color(0xFFFF4F68)
private val ElectricYellow = Color(0xFFFFD833)

val themeLightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    surface = SurfaceColor,
    onSurface = OnSurfaceColor,
    surfaceVariant = SurfaceVariantColor,
    background = BackgroundColor,
    onBackground = OnBackgroundColor,
    onPrimary = OnPrimaryColor,
    surfaceDim = SurfaceDimColor,
)

fun lightCustomColors(
    grassGreen: Color = GrassGreen,
    fireRed: Color = FireRed,
    electricYellow: Color = ElectricYellow,
) = CustomColors(
    grassGreen = grassGreen,
    fireRed = fireRed,
    electricYellow = electricYellow,
)

@Immutable
data class CustomColors(
    val grassGreen: Color = Color.Unspecified,
    val fireRed: Color = Color.Unspecified,
    val electricYellow: Color = Color.Unspecified,
)

val LocalCustomColors = staticCompositionLocalOf { CustomColors() }