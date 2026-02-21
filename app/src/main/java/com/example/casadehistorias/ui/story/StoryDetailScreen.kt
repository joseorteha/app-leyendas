package com.example.casadehistorias.ui.story

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.casadehistorias.domain.model.Story
import com.example.casadehistorias.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryDetailScreen(
    story: Story,
    onNavigateBack: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0f) }

    // Simular reproducción de audio
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (isPlaying && currentPosition < 100f) {
                kotlinx.coroutines.delay(100)
                currentPosition += 0.5f
                if (currentPosition >= 100f) {
                    currentPosition = 100f
                    isPlaying = false
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = LunaLlena
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NocheProfunda
                )
            )
        },
        containerColor = NocheProfunda,
        bottomBar = {
            AudioPlayerBar(
                isPlaying = isPlaying,
                currentPosition = currentPosition,
                onPlayPauseClick = { 
                    if (currentPosition >= 100f) {
                        currentPosition = 0f
                    }
                    isPlaying = !isPlaying 
                },
                onSeek = { 
                    currentPosition = it
                    if (currentPosition >= 100f) {
                        isPlaying = false
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header con gradiente
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FondoTarjeta
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    AguaSagrada.copy(alpha = 0.15f),
                                    Color.Transparent
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = story.titleEs,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = LunaLlena,
                            fontSize = 28.sp,
                            lineHeight = 34.sp
                        )

                        Text(
                            text = story.titleNahuatl,
                            style = MaterialTheme.typography.titleLarge,
                            fontStyle = FontStyle.Italic,
                            color = AguaSagrada,
                            fontSize = 20.sp
                        )

                        Divider(
                            color = AguaSagrada.copy(alpha = 0.3f),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = TextoSecundario,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Narrado por ${story.narratorName}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextoSecundario,
                                fontSize = 15.sp
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = TextoSecundario,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Sierra de Zongolica, Veracruz",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextoSecundario,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }

            // Contenido de la historia
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FondoTarjeta
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "La Historia",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AguaSagrada,
                        fontSize = 20.sp
                    )

                    Text(
                        text = story.contentEs,
                        style = MaterialTheme.typography.bodyLarge,
                        color = LunaLlena,
                        fontSize = 16.sp,
                        lineHeight = 26.sp
                    )

                    if (story.contentNahuatl.isNotEmpty()) {
                        Divider(
                            color = AguaSagrada.copy(alpha = 0.2f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        Text(
                            text = "En Náhuatl",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AguaSagrada,
                            fontSize = 18.sp
                        )

                        Text(
                            text = story.contentNahuatl,
                            style = MaterialTheme.typography.bodyLarge,
                            fontStyle = FontStyle.Italic,
                            color = LunaLlena.copy(alpha = 0.9f),
                            fontSize = 16.sp,
                            lineHeight = 26.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun AudioPlayerBar(
    isPlaying: Boolean,
    currentPosition: Float,
    onPlayPauseClick: () -> Unit,
    onSeek: (Float) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = FondoTarjeta
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Barra de progreso
            Column {
                Slider(
                    value = currentPosition,
                    onValueChange = onSeek,
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = AguaSagrada,
                        activeTrackColor = AguaSagrada,
                        inactiveTrackColor = TextoSecundario.copy(alpha = 0.3f)
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime((currentPosition * 2.25f).toInt()),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "3:45",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario,
                        fontSize = 12.sp
                    )
                }
            }

            // Controles de reproducción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* TODO: Previous */ }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reiniciar",
                        tint = LunaLlena,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                // Botón de play/pause grande
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(AguaSagrada, AguaSagradaOscura)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = onPlayPauseClick,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Close else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pausar" else "Reproducir",
                            tint = NocheProfunda,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(24.dp))

                IconButton(onClick = { /* TODO: Next */ }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Compartir",
                        tint = LunaLlena,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "$mins:${secs.toString().padStart(2, '0')}"
}
