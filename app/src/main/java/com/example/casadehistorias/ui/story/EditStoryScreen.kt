package com.example.casadehistorias.ui.story

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.AddAPhoto
import coil3.compose.AsyncImage
import com.example.casadehistorias.domain.model.Story
import com.example.casadehistorias.ui.theme.*
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.annotations.MarkerOptions
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStoryScreen(
    story: Story,
    onNavigateBack: () -> Unit,
    viewModel: AddStoryViewModel = hiltViewModel()
) {
    var titleEs by remember { mutableStateOf(story.titleEs) }
    var titleNahuatl by remember { mutableStateOf(story.titleNahuatl) }
    var contentEs by remember { mutableStateOf(story.contentEs) }
    var contentNahuatl by remember { mutableStateOf(story.contentNahuatl) }
    var narratorName by remember { mutableStateOf(story.narratorName) }
    var community by remember { mutableStateOf(story.community) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    var selectedLat by remember { mutableStateOf(story.latitude) }
    var selectedLng by remember { mutableStateOf(story.longitude) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    val uiState by viewModel.uiState.collectAsState()
    val storyViewModel: StoryViewModel = hiltViewModel()

    LaunchedEffect(uiState) {
        if (uiState is AddStoryUiState.Success) {
            viewModel.resetState()
            onNavigateBack()
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Historia", color = LunaLlena) },
            text = { Text("¿Estás seguro de que quieres eliminar esta historia? Esta acción no se puede deshacer.", color = TextoSecundario) },
            confirmButton = {
                TextButton(
                    onClick = {
                        storyViewModel.deleteStory(story.id)
                        showDeleteDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text("Eliminar", color = FloresCeremoniales)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar", color = AguaSagrada)
                }
            },
            containerColor = FondoTarjeta
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Historia", color = LunaLlena, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = LunaLlena)
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = FloresCeremoniales)
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // General info card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FondoTarjeta)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Datos Generales", style = MaterialTheme.typography.labelLarge, color = AguaSagrada)
                    
                    // Image Selector
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { imagePickerLauncher.launch("image/*") }
                            .then(
                                if (selectedImageUri == null && story.imageUrl.isNullOrBlank()) {
                                    Modifier.background(NocheProfunda.copy(alpha = 0.5f))
                                } else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedImageUri != null || !story.imageUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = selectedImageUri ?: story.imageUrl,
                                contentDescription = "Imagen seleccionada",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = AguaSagrada, modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Añadir foto de portada", color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    CustomOutlinedTextField(value = titleEs, onValueChange = { titleEs = it }, label = "Título en Español", icon = Icons.Outlined.Edit)
                    CustomOutlinedTextField(value = titleNahuatl, onValueChange = { titleNahuatl = it }, label = "Título en Náhuatl", icon = Icons.Outlined.Edit)
                    CustomOutlinedTextField(value = narratorName, onValueChange = { narratorName = it }, label = "Nombre del Narrador", icon = Icons.Outlined.Person)
                    CustomOutlinedTextField(value = community, onValueChange = { community = it }, label = "Comunidad de origen", icon = Icons.Outlined.LocationOn)
                    
                    Text(
                        "Ubicación en el Mapa (Opcional)\nToca el mapa para fijar un punto.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario
                    )

                    // MapLibre Location Picker
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = { context ->
                                MapLibre.getInstance(context)
                                val mapView = MapView(context)
                                mapView.onCreate(null)
                                mapView.getMapAsync { map ->
                                    map.setStyle(Style.Builder().fromUri("https://tiles.openfreemap.org/styles/liberty")) {
                                        // Set camera to target or default
                                        val lat = selectedLat ?: 18.6667
                                        val lng = selectedLng ?: -97.0000
                                        map.cameraPosition = CameraPosition.Builder()
                                            .target(LatLng(lat, lng))
                                            .zoom(if (selectedLat != null) 12.0 else 8.0)
                                            .build()

                                        if (selectedLat != null && selectedLng != null) {
                                            map.addMarker(
                                                MarkerOptions()
                                                    .position(LatLng(selectedLat!!, selectedLng!!))
                                                    .title("Ubicación actual")
                                            )
                                        }

                                        map.addOnMapClickListener { point ->
                                            map.clear()
                                            map.addMarker(
                                                MarkerOptions()
                                                    .position(point)
                                                    .title("Nueva ubicación")
                                            )
                                            selectedLat = point.latitude
                                            selectedLng = point.longitude
                                            true
                                        }
                                    }
                                }
                                mapView
                            },
                            update = { mapView ->
                                mapView.onResume()
                            }
                        )
                    }
                }
            }

            // Story content card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FondoTarjeta)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("La Historia", style = MaterialTheme.typography.labelLarge, color = AguaSagrada)
                    CustomOutlinedTextField(value = contentEs, onValueChange = { contentEs = it }, label = "Historia en Español", icon = Icons.Outlined.Description, modifier = Modifier.heightIn(min = 120.dp), singleLine = false)
                    CustomOutlinedTextField(value = contentNahuatl, onValueChange = { contentNahuatl = it }, label = "Historia en Náhuatl (Opcional)", icon = Icons.Outlined.Description, modifier = Modifier.heightIn(min = 120.dp), singleLine = false)
                }
            }

            if (uiState is AddStoryUiState.Error) {
                Text(
                    text = (uiState as AddStoryUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (titleEs.isNotBlank() && contentEs.isNotBlank() && narratorName.isNotBlank()) {
                        viewModel.updateStory(
                            story.copy(
                                titleEs = titleEs,
                                titleNahuatl = titleNahuatl,
                                contentEs = contentEs,
                                contentNahuatl = contentNahuatl,
                                narratorName = narratorName,
                                community = community,
                                latitude = selectedLat,
                                longitude = selectedLng
                            ),
                            newImageUri = selectedImageUri
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AguaSagrada, contentColor = NocheProfunda),
                enabled = uiState !is AddStoryUiState.Loading
            ) {
                if (uiState is AddStoryUiState.Loading) {
                    CircularProgressIndicator(color = NocheProfunda, modifier = Modifier.size(24.dp))
                } else {
                    Text("Guardar Cambios", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
