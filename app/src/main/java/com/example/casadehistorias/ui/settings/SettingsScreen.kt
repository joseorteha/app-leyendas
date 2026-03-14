package com.example.casadehistorias.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.casadehistorias.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Ajustes", fontWeight = FontWeight.Bold, color = LunaLlena)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // About section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FondoTarjeta)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = AguaSagrada)
                        Text(
                            "Acerca de",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AguaSagrada
                        )
                    }

                    Text(
                        "Casa de Historias",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = LunaLlena
                    )
                    Text(
                        "Una aplicación dedicada a preservar y compartir " +
                        "las leyendas ancestrales de la Sierra de Zongolica, " +
                        "manteniendo vivas las voces de nuestros abuelos " +
                        "en español y náhuatl.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario
                    )

                    HorizontalDivider(color = TextoSecundario.copy(alpha = 0.2f))

                    Text(
                        "Versión 1.0",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextoSecundario
                    )
                }
            }

            // Credits
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FondoTarjeta)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("🌙", style = MaterialTheme.typography.displaySmall)
                    Text(
                        "Tlajtolkali",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AguaSagrada
                    )
                    Text(
                        "Preservando el conocimiento\nde generación en generación",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
