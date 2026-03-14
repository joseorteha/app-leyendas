package com.example.casadehistorias.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.casadehistorias.domain.model.Story
import com.example.casadehistorias.ui.story.StoryViewModel
import com.example.casadehistorias.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    viewModel: StoryViewModel,
    onStoryClick: (String) -> Unit,
    onNavigateToStoryList: () -> Unit,
    onNavigateToDictionary: () -> Unit
) {
    val stories by viewModel.stories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Explorar",
                        fontWeight = FontWeight.Bold,
                        color = LunaLlena
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = NocheProfunda
                )
            )
        },
        containerColor = NocheProfunda
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AguaSagrada)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    SectionTitle(
                        title = "Lo más nuevo",
                        actionText = "Ver todo",
                        onActionClick = onNavigateToStoryList
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    val recentStories = stories.sortedByDescending { it.createdAt }.take(5)
                    
                    if (recentStories.isNotEmpty()) {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(recentStories) { story ->
                                FeaturedStoryCard(story = story, onClick = { onStoryClick(story.id) })
                            }
                        }
                    } else {
                        EmptyStateMessage("No hay historias nuevas.")
                    }
                }

                item {
                    SectionTitle(title = "Con Audio Original")
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    val audioStories = stories.filter { !it.audioUrl.isNullOrBlank() }.take(5)
                    
                    if (audioStories.isNotEmpty()) {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            audioStories.forEach { story ->
                                CompactStoryCard(story = story, onClick = { onStoryClick(story.id) })
                            }
                        }
                    } else {
                        EmptyStateMessage("Aún no hay audios grabados.")
                    }
                }

                item {
                    SectionTitle(title = "Aprender")
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
                        onClick = onNavigateToDictionary,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(NocheProfunda, AguaSagradaOscura.copy(alpha = 0.5f))
                                        )
                                    )
                            )
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Diccionario Náhuatl",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = LunaLlena
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Aprende palabras de la Sierra y su significado.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextoSecundario
                                    )
                                }
                                
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(AguaSagrada.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = AguaSagrada)
                                }
                            }
                        }
                    }
                }
                
                item {
                    SectionTitle(title = "Por Comunidad")
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    val communities = stories.map { it.community }.filter { it.isNotBlank() }.distinct().take(10)
                    
                    if (communities.isNotEmpty()) {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(communities) { community ->
                                FilterChip(
                                    selected = false,
                                    onClick = onNavigateToStoryList, // Ideally filter the list, but for now navigate to list
                                    label = { Text(community) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        containerColor = FondoTarjeta,
                                        labelColor = LunaLlena
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = false,
                                        borderColor = AguaSagrada.copy(alpha = 0.3f)
                                    )
                                )
                            }
                        }
                    } else {
                        EmptyStateMessage("No hay comunidades registradas.")
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String, actionText: String? = null, onActionClick: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = LunaLlena
        )
        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.bodyMedium,
                color = AguaSagrada,
                modifier = Modifier.clickable { onActionClick() }
            )
        }
    }
}

@Composable
fun EmptyStateMessage(message: String) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodyMedium,
        color = TextoSecundario,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeaturedStoryCard(story: Story, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .height(320.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
        onClick = onClick,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (!story.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = story.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, NocheProfunda.copy(alpha = 0.8f), NocheProfunda)
                            )
                        )
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(FondoTarjeta, SurfaceVariant)
                            )
                        )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                if (story.titleNahuatl.isNotBlank()) {
                    Text(
                        text = story.titleNahuatl,
                        style = MaterialTheme.typography.titleMedium,
                        fontStyle = FontStyle.Italic,
                        color = AguaSagrada,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = story.titleEs,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = LunaLlena,
                    maxLines = 2,
                    lineHeight = 26.sp,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Outlined.Person, contentDescription = null, tint = TextoSecundario, modifier = Modifier.size(14.dp))
                    Text(
                        text = story.narratorName,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactStoryCard(story: Story, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
        onClick = onClick,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                    .background(SurfaceVariant)
            ) {
                if (!story.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = story.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                
                if (!story.audioUrl.isNullOrBlank()) {
                    Box(
                        modifier = Modifier.fillMaxSize().background(NocheProfunda.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(AguaSagrada),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Audio", tint = NocheProfunda, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = story.titleEs,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = LunaLlena,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (story.titleNahuatl.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = story.titleNahuatl,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = AguaSagrada,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
