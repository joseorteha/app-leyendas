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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.casadehistorias.domain.model.Story
import com.example.casadehistorias.ui.favorites.FavoritesViewModel
import com.example.casadehistorias.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryDetailScreen(
    story: Story,
    favoritesViewModel: FavoritesViewModel,
    onNavigateBack: () -> Unit,
    onEditClick: () -> Unit = {}
) {
    val isFavorite by remember(story.id) {
        derivedStateOf { favoritesViewModel.isFavorite(story.id) }
    }
    val favoriteIds by favoritesViewModel.favoriteIds.collectAsState()
    val currentIsFav = favoriteIds.contains(story.id)

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
                actions = {
                    IconButton(onClick = { favoritesViewModel.toggleFavorite(story.id) }) {
                        Icon(
                            imageVector = if (currentIsFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (currentIsFav) "Quitar de favoritos" else "Agregar a favoritos",
                            tint = if (currentIsFav) FloresCeremoniales else LunaLlena
                        )
                    }
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = LunaLlena)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NocheProfunda
                )
            )
        },
        containerColor = NocheProfunda
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Cover image (if available)
            if (!story.imageUrl.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) {
                    AsyncImage(
                        model = story.imageUrl,
                        contentDescription = story.titleEs,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, NocheProfunda),
                                    startY = 150f
                                )
                            )
                    )
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = FondoTarjeta)
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

                            HorizontalDivider(
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

                            if (story.community.isNotBlank()) {
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
                                        text = story.community,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = TextoSecundario,
                                        fontSize = 15.sp
                                    )
                                }
                            }

                            // Tags
                            if (story.tags.isNotEmpty()) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    story.tags.forEach { tag ->
                                        SuggestionChip(
                                            onClick = {},
                                            label = {
                                                Text(tag, fontSize = 12.sp, color = AguaSagrada)
                                            },
                                            border = SuggestionChipDefaults.suggestionChipBorder(
                                                true,
                                                borderColor = AguaSagrada.copy(alpha = 0.5f)
                                            ),
                                            colors = SuggestionChipDefaults.suggestionChipColors(
                                                containerColor = AguaSagrada.copy(alpha = 0.1f)
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Story content
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = FondoTarjeta)
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
                            HorizontalDivider(
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

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryDetailLoadingScreen(
    onNavigateBack: () -> Unit
) {
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
        containerColor = NocheProfunda
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = AguaSagrada)
        }
    }
}
