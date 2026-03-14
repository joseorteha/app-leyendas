package com.example.casadehistorias.ui.dictionary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.casadehistorias.ui.theme.*

data class NahuatlWord(val nahuatl: String, val spanish: String, val example: String = "")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen(
    onNavigateBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    
    // Lista estática simulando una base de datos de palabras
    val dictionary = listOf(
        NahuatlWord("Tonatiuh", "Sol", "El dador de vida."),
        NahuatlWord("Metztli", "Luna", "La que brilla en la noche mística."),
        NahuatlWord("Tlalticpac", "Tierra / Mundo", "Sobre la tierra donde caminamos."),
        NahuatlWord("Atl", "Agua", "Agua sagrada que da vida."),
        NahuatlWord("Ehecatl", "Viento", "El viento que trae las voces."),
        NahuatlWord("Tletl", "Fuego", "El abuelo fuego."),
        NahuatlWord("Amoxmatqui", "Lector / Sabio", "Quien conoce el libro."),
        NahuatlWord("Cintli", "Maíz", "Nuestro sustento."),
        NahuatlWord("Kuali", "Bueno", "Algo que está bien."),
        NahuatlWord("Tlazocamati", "Gracias", "Agradecimiento profundo.")
    )
    
    val filteredDictionary = dictionary.filter {
        it.nahuatl.contains(searchQuery, ignoreCase = true) || 
        it.spanish.contains(searchQuery, ignoreCase = true)
    }.sortedBy { it.nahuatl }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text("Cultura y Diccionario", fontWeight = FontWeight.Bold, color = LunaLlena) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = LunaLlena)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NocheProfunda)
            )
        },
        containerColor = NocheProfunda
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar palabra...") },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Buscar", tint = AguaSagrada) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AguaSagrada,
                    unfocusedBorderColor = TextoSecundario.copy(alpha = 0.3f),
                    focusedLabelColor = AguaSagrada,
                    unfocusedLabelColor = TextoSecundario,
                    cursorColor = AguaSagrada,
                    focusedTextColor = LunaLlena,
                    unfocusedTextColor = LunaLlena
                )
            )
            
            Text(
                text = "Palabras Comunes (${filteredDictionary.size})",
                style = MaterialTheme.typography.titleMedium,
                color = AguaSagrada,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredDictionary) { word ->
                    WordCard(word)
                }
            }
        }
    }
}

@Composable
fun WordCard(word: NahuatlWord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
        border = androidx.compose.foundation.BorderStroke(1.dp, androidx.compose.ui.graphics.Color.White.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = word.nahuatl,
                    style = MaterialTheme.typography.titleLarge,
                    color = AguaSagrada,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = word.spanish,
                    style = MaterialTheme.typography.titleMedium,
                    color = LunaLlena,
                    fontWeight = FontWeight.Medium
                )
            }
            if (word.example.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceVariant, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Ej: ${word.example}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}
