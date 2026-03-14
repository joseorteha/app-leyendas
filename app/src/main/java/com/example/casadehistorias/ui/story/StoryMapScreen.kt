package com.example.casadehistorias.ui.story

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.casadehistorias.ui.theme.*
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.annotations.MarkerOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryMapScreen(
    viewModel: StoryViewModel,
    onStoryClick: (String) -> Unit
) {
    val stories by viewModel.stories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Sierra de Zongolica center
    val zongolicaLat = 18.6667
    val zongolicaLng = -97.0000

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Mapa de Leyendas",
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
            val storiesWithLocation = stories.filter { it.latitude != null && it.longitude != null }
            
            var selectedStory by remember { mutableStateOf<com.example.casadehistorias.domain.model.Story?>(null) }
            var showSheet by remember { mutableStateOf(false) }

            if (storiesWithLocation.isEmpty()) {
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
                        Text("🗺️", fontSize = 64.sp)
                        Text(
                            "Sin ubicaciones",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = LunaLlena
                        )
                        Text(
                            "Aún no hay historias con\nubicación en el mapa",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoSecundario,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                // MapLibre map using AndroidView
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    factory = { context ->
                        MapLibre.getInstance(context)
                        val mapView = MapView(context)
                        mapView.onCreate(null)
                        mapView.getMapAsync { map ->
                            map.setStyle(
                                Style.Builder().fromUri("https://tiles.openfreemap.org/styles/liberty")
                            ) {
                                // Set camera to Sierra de Zongolica
                                map.cameraPosition = CameraPosition.Builder()
                                    .target(LatLng(zongolicaLat, zongolicaLng))
                                    .zoom(9.0)
                                    .build()

                                // Add markers for stories with location
                                storiesWithLocation.forEach { story ->
                                    map.addMarker(
                                        MarkerOptions()
                                            .position(LatLng(story.latitude!!, story.longitude!!))
                                            .title(story.titleEs)
                                            .snippet("Narrado por ${story.narratorName}")
                                    )
                                }

                                map.setOnMarkerClickListener { marker ->
                                    selectedStory = storiesWithLocation.find { it.titleEs == marker.title }
                                    if (selectedStory != null) {
                                        showSheet = true
                                    }
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

                if (showSheet && selectedStory != null) {
                    ModalBottomSheet(
                        onDismissRequest = { showSheet = false },
                        containerColor = FondoTarjeta,
                        dragHandle = { BottomSheetDefaults.DragHandle(color = AguaSagrada) }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = selectedStory!!.titleEs,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = LunaLlena
                            )
                            
                            if (selectedStory!!.titleNahuatl.isNotBlank()) {
                                Text(
                                    text = selectedStory!!.titleNahuatl,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    color = AguaSagrada
                                )
                            }
                            
                            Text(
                                text = selectedStory!!.contentEs,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextoSecundario,
                                maxLines = 3,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                            
                            Button(
                                onClick = { 
                                    showSheet = false
                                    onStoryClick(selectedStory!!.id) 
                                },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AguaSagrada, contentColor = NocheProfunda),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Leer leyenda completa", fontWeight = FontWeight.Bold)
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}
