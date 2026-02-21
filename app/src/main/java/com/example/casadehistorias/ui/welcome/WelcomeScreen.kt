package com.example.casadehistorias.ui.welcome

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.casadehistorias.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(onContinue: () -> Unit) {
    // Animaciones
    val infiniteTransition = rememberInfiniteTransition(label = "welcome")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        NocheProfunda,
                        FondoTarjeta,
                        NocheProfunda
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .alpha(if (visible) 1f else 0f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(modifier = Modifier.weight(0.5f))

            // Logo/Título animado
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.scale(scale)
            ) {
                Text(
                    text = "🌙",
                    fontSize = 80.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Casa de Historias",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = LunaLlena,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 48.sp
                )

                Text(
                    text = "Tlajtolkali",
                    style = MaterialTheme.typography.headlineMedium,
                    fontStyle = FontStyle.Italic,
                    color = AguaSagrada,
                    fontSize = 28.sp
                )
            }

            // Descripción
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Leyendas ancestrales de la",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextoSecundario,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Sierra de Zongolica",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = LunaLlena,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón de continuar
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AguaSagrada,
                    contentColor = NocheProfunda
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Descubrir Historias",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
