package com.example.casadehistorias.ui.welcome

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.casadehistorias.ui.theme.*

@Composable
fun WelcomeScreen(
    onContinue: () -> Unit
) {
    var showTitle by remember { mutableStateOf(false) }
    var showSubtitle by remember { mutableStateOf(false) }
    var showNahuatl by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(400)
        showTitle = true
        delay(600)
        showSubtitle = true
        delay(600)
        showNahuatl = true
        delay(600)
        showButton = true
    }

    // Breathing glow animation
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        AguaSagradaOscura.copy(alpha = 0.2f),
                        NocheProfunda
                    ),
                    radius = 1200f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 36.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Moon emoji with glow
            AnimatedVisibility(
                visible = showTitle,
                enter = fadeIn(tween(800)) + scaleIn(tween(800))
            ) {
                Text(
                    text = "🌙",
                    fontSize = 80.sp,
                    modifier = Modifier.alpha(glowAlpha)
                )
            }

            // Title
            AnimatedVisibility(
                visible = showTitle,
                enter = fadeIn(tween(800)) + slideInVertically(
                    tween(800),
                    initialOffsetY = { 50 }
                )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Casa de",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Light,
                        color = LunaLlena,
                        fontSize = 36.sp
                    )
                    Text(
                        text = "Historias",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = AguaSagrada,
                        fontSize = 48.sp
                    )
                }
            }

            // Subtitle
            AnimatedVisibility(
                visible = showSubtitle,
                enter = fadeIn(tween(800)) + slideInVertically(
                    tween(800),
                    initialOffsetY = { 30 }
                )
            ) {
                Text(
                    text = "Leyendas ancestrales de la\nSierra de Zongolica",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextoSecundario,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }

            // Nahuatl phrase
            AnimatedVisibility(
                visible = showNahuatl,
                enter = fadeIn(tween(1000))
            ) {
                Text(
                    text = "\"Tlajtolkali\"",
                    style = MaterialTheme.typography.headlineSmall,
                    fontStyle = FontStyle.Italic,
                    color = AguaSagrada.copy(alpha = 0.7f),
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Button
            AnimatedVisibility(
                visible = showButton,
                enter = fadeIn(tween(800)) + scaleIn(tween(800))
            ) {
                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AguaSagrada,
                        contentColor = NocheProfunda
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Text(
                        "Descubrir Historias",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
