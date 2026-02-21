package com.example.casadehistorias.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Casa de Historias - Tema "Noche Mística"
 * Un tema que transporta al usuario a una noche ancestral en la sierra
 */
private val CasaDeHistoriasColorScheme = darkColorScheme(
    primary = AguaSagrada,
    onPrimary = NocheProfunda,
    primaryContainer = AguaSagradaOscura,
    onPrimaryContainer = LunaLlena,

    secondary = FloresCeremoniales,
    onSecondary = LunaLlena,
    secondaryContainer = FloresCeremonialesOscuras,
    onSecondaryContainer = LunaLlena,

    background = NocheProfunda,
    onBackground = LunaLlena,
    surface = FondoTarjeta,
    onSurface = LunaLlena,

    outline = TextoSecundario,
    outlineVariant = TextoSecundario.copy(alpha = 0.5f),

    surfaceVariant = FondoTarjeta,
    onSurfaceVariant = TextoSecundario,

    error = FloresCeremoniales,
    onError = LunaLlena
)

private val DarkColorScheme = darkColorScheme(
    primary = Turquesa,
    secondary = RedFlores,
    background = BlueNight,
    surface = SurfaceBlue,
    onPrimary = WhitePure,
    onSecondary = WhitePure,
    onBackground = WhitePure,
    onSurface = WhitePure,
    error = RedFlores
)

private val LightColorScheme = lightColorScheme(
    primary = Turquesa,
    secondary = RedFlores,
    background = BlueNight,
    surface = SurfaceBlue,
    onPrimary = WhitePure,
    onSecondary = WhitePure,
    onBackground = WhitePure,
    onSurface = WhitePure,
    error = RedFlores
)

@Composable
fun CasaDeHistoriasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> CasaDeHistoriasColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
