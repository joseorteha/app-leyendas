package com.example.casadehistorias.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.casadehistorias.ui.story.ModernStoryCard
import com.example.casadehistorias.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onStoryClick: (String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsState()
    val results by viewModel.results.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = query,
                        onValueChange = viewModel::onQueryChange,
                        placeholder = { Text("Buscar historias, narradores...", color = TextoSecundario) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = TextoSecundario)
                        },
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = { viewModel.clearSearch() }) {
                                    Icon(Icons.Default.Close, contentDescription = "Limpiar", tint = TextoSecundario)
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AguaSagrada,
                            unfocusedBorderColor = TextoSecundario.copy(alpha = 0.3f),
                            cursorColor = AguaSagrada,
                            focusedTextColor = LunaLlena,
                            unfocusedTextColor = LunaLlena
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
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
        when {
            isSearching -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AguaSagrada)
                }
            }
            query.isNotEmpty() && results.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("🔍", fontSize = 64.sp)
                        Text(
                            "Sin resultados",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = LunaLlena
                        )
                        Text(
                            "No se encontraron historias\npara \"$query\"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoSecundario,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            query.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("📜", fontSize = 64.sp)
                        Text(
                            "Buscar Historias",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = LunaLlena
                        )
                        Text(
                            "Busca por título, narrador,\ncomunidad o etiqueta",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoSecundario,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            "${results.size} resultado${if (results.size != 1) "s" else ""}",
                            style = MaterialTheme.typography.labelLarge,
                            color = TextoSecundario,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    items(results) { story ->
                        ModernStoryCard(
                            story = story,
                            onClick = { onStoryClick(story.id) }
                        )
                    }
                }
            }
        }
    }
}
