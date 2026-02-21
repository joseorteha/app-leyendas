package com.example.casadehistorias.ui.story

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.example.casadehistorias.ui.theme.NocheProfunda
import com.google.android.gms.maps.model.MapStyleOptions


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryMapScreen(
    viewModel: StoryViewModel,
    onStoryClick: (String) -> Unit
) {
    val stories by viewModel.stories.collectAsState()
    
    // Posición inicial: Centro de la Sierra de Zongolica
    val zongolica = LatLng(18.6667, -97.0000)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(zongolica, 10f)
    }

    Scaffold(
        containerColor = NocheProfunda
    ) { padding ->
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                mapStyleOptions = MapStyleOptions(MapStyle.NIGHT_STYLE) // Estilo noche para combinar
            )
        ) {
            stories.forEach { story ->
                if (story.latitude != null && story.longitude != null) {
                    Marker(
                        state = MarkerState(position = LatLng(story.latitude, story.longitude)),
                        title = story.titleEs,
                        snippet = "Narrado por ${story.narratorName}",
                        onClick = {
                            onStoryClick(story.id)
                            true
                        }
                    )
                }
            }
        }
    }
}

// Objeto para el estilo oscuro del mapa
object MapStyle {
    const val NIGHT_STYLE = """
    [
      { "elementType": "geometry", "stylers": [{ "color": "#242f3e" }] },
      { "elementType": "labels.text.fill", "stylers": [{ "color": "#746855" }] },
      { "elementType": "labels.text.stroke", "stylers": [{ "color": "#242f3e" }] },
      { "featureType": "administrative.locality", "elementType": "labels.text.fill", "stylers": [{ "color": "#d59563" }] },
      { "featureType": "road", "elementType": "geometry", "stylers": [{ "color": "#38414e" }] },
      { "featureType": "road", "elementType": "geometry.stroke", "stylers": [{ "color": "#212a37" }] },
      { "featureType": "water", "elementType": "geometry", "stylers": [{ "color": "#17263c" }] }
    ]
    """
}
