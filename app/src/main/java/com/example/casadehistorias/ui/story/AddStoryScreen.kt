package com.example.casadehistorias.ui.story

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.casadehistorias.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddStoryViewModel = hiltViewModel()
) {
    var titleEs by remember { mutableStateOf("") }
    var titleNahuatl by remember { mutableStateOf("") }
    var contentEs by remember { mutableStateOf("") }
    var contentNahuatl by remember { mutableStateOf("") }
    var narratorName by remember { mutableStateOf("") }
    var community by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is AddStoryUiState.Success) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Subir Nueva Historia", color = LunaLlena) },
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
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Preservar el conocimiento",
                    style = MaterialTheme.typography.titleMedium,
                    color = AguaSagrada
                )

                OutlinedTextField(
                    value = titleEs,
                    onValueChange = { titleEs = it },
                    label = { Text("Título en Español") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors()
                )

                OutlinedTextField(
                    value = titleNahuatl,
                    onValueChange = { titleNahuatl = it },
                    label = { Text("Título en Náhuatl") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors()
                )

                OutlinedTextField(
                    value = narratorName,
                    onValueChange = { narratorName = it },
                    label = { Text("Nombre del Narrador (Abuelo/a)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors()
                )

                OutlinedTextField(
                    value = community,
                    onValueChange = { community = it },
                    label = { Text("Comunidad de origen") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors()
                )

                OutlinedTextField(
                    value = contentEs,
                    onValueChange = { contentEs = it },
                    label = { Text("Historia en Español") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp),
                    colors = textFieldColors(),
                    maxLines = 10
                )

                OutlinedTextField(
                    value = contentNahuatl,
                    onValueChange = { contentNahuatl = it },
                    label = { Text("Historia en Náhuatl (Opcional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp),
                    colors = textFieldColors(),
                    maxLines = 10
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (titleEs.isNotBlank() && contentEs.isNotBlank() && narratorName.isNotBlank()) {
                            viewModel.addStory(
                                titleEs = titleEs,
                                titleNahuatl = titleNahuatl,
                                contentEs = contentEs,
                                contentNahuatl = contentNahuatl,
                                narratorName = narratorName,
                                community = community
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AguaSagrada,
                        contentColor = NocheProfunda
                    ),
                    enabled = uiState !is AddStoryUiState.Loading
                ) {
                    if (uiState is AddStoryUiState.Loading) {
                        CircularProgressIndicator(color = NocheProfunda, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Guardar Historia en la Nube", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                if (uiState is AddStoryUiState.Error) {
                    Text(
                        text = (uiState as AddStoryUiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = LunaLlena,
    unfocusedTextColor = LunaLlena,
    focusedLabelColor = AguaSagrada,
    unfocusedLabelColor = TextoSecundario,
    focusedBorderColor = AguaSagrada,
    unfocusedBorderColor = FondoTarjeta,
    cursorColor = AguaSagrada
)
