package com.example.casadehistorias.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
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

    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextoSecundario,

    error = FloresCeremoniales,
    onError = LunaLlena
)

@Composable
fun CasaDeHistoriasTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = CasaDeHistoriasColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = NocheProfunda.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
