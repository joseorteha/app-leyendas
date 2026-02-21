package com.example.casadehistorias.ui.story

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
fun StoryListScreen(
    viewModel: StoryViewModel,
    onStoryClick: (String) -> Unit = {},
    onAddStoryClick: () -> Unit = {} // Nuevo parámetro
) {
    val stories by viewModel.stories.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Casa de Historias",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = LunaLlena
                        )
                        Text(
                            text = "Leyendas de la Sierra",
                            style = MaterialTheme.typography.labelSmall,
                            color = AguaSagrada,
                            fontSize = 11.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = NocheProfunda
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddStoryClick,
                containerColor = AguaSagrada,
                contentColor = NocheProfunda,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Historia")
            }
        },
        containerColor = NocheProfunda
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(stories) { story ->
                ModernStoryCard(
                    story = story,
                    onClick = { onStoryClick(story.id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernStoryCard(
    story: Story,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = FondoTarjeta
        ),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                AguaSagrada.copy(alpha = 0.05f),
                                Color.Transparent,
                                FloresCeremoniales.copy(alpha = 0.05f)
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = story.titleEs,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = LunaLlena,
                            fontSize = 20.sp,
                            lineHeight = 24.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = story.titleNahuatl,
                            style = MaterialTheme.typography.titleMedium,
                            fontStyle = FontStyle.Italic,
                            color = AguaSagrada,
                            fontSize = 16.sp,
                            lineHeight = 20.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            tint = TextoSecundario,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = story.narratorName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoSecundario,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    AguaSagrada,
                                    AguaSagradaOscura
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { /* TODO: Reproducir Audio */ },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Reproducir historia",
                            tint = NocheProfunda,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}
