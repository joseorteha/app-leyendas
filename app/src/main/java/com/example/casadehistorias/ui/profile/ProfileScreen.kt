package com.example.casadehistorias.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.casadehistorias.ui.auth.AuthViewModel
import com.example.casadehistorias.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val userProfile by authViewModel.userProfile.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf("") }
    var editCommunity by remember { mutableStateOf("") }

    LaunchedEffect(userProfile) {
        userProfile?.let {
            editName = it.displayName
            editCommunity = it.community
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Mi Perfil", fontWeight = FontWeight.Bold, color = LunaLlena)
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes", tint = LunaLlena)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
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
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(AguaSagrada, AguaSagradaOscura)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = NocheProfunda,
                    modifier = Modifier.size(64.dp)
                )
            }

            // Name
            Text(
                text = userProfile?.displayName ?: "Usuario",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = LunaLlena
            )
            Text(
                text = userProfile?.email ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario
            )

            // Info card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FondoTarjeta)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Información Personal",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AguaSagrada
                        )
                        IconButton(onClick = { isEditing = !isEditing }) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = AguaSagrada
                            )
                        }
                    }

                    if (isEditing) {
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AguaSagrada,
                                unfocusedBorderColor = TextoSecundario.copy(alpha = 0.5f),
                                focusedLabelColor = AguaSagrada,
                                unfocusedLabelColor = TextoSecundario,
                                cursorColor = AguaSagrada,
                                focusedTextColor = LunaLlena,
                                unfocusedTextColor = LunaLlena
                            ),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = editCommunity,
                            onValueChange = { editCommunity = it },
                            label = { Text("Comunidad") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AguaSagrada,
                                unfocusedBorderColor = TextoSecundario.copy(alpha = 0.5f),
                                focusedLabelColor = AguaSagrada,
                                unfocusedLabelColor = TextoSecundario,
                                cursorColor = AguaSagrada,
                                focusedTextColor = LunaLlena,
                                unfocusedTextColor = LunaLlena
                            ),
                            singleLine = true
                        )
                        Button(
                            onClick = {
                                authViewModel.updateProfile(editName, editCommunity)
                                isEditing = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AguaSagrada,
                                contentColor = NocheProfunda
                            )
                        ) {
                            Text("Guardar Cambios", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        ProfileInfoRow("Nombre", userProfile?.displayName ?: "-")
                        ProfileInfoRow("Comunidad", userProfile?.community ?: "-")
                        ProfileInfoRow("Correo", userProfile?.email ?: "-")
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Logout
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = FloresCeremoniales
                ),
                border = ButtonDefaults.outlinedButtonBorder(true).copy(
                    brush = Brush.horizontalGradient(
                        listOf(FloresCeremoniales, FloresCeremonialesOscuras)
                    )
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ProfileInfoRow(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelMedium, color = TextoSecundario)
        Text(value, style = MaterialTheme.typography.bodyLarge, color = LunaLlena, fontSize = 16.sp)
    }
}
