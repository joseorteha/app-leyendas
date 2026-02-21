package com.example.casadehistorias.domain.model

data class Story(
    val id: String,
    val titleEs: String,      // Título en Español
    val titleNahuatl: String, // Título en Náhuatl
    val contentEs: String,
    val contentNahuatl: String,
    val audioUrl: String?,    // Ruta al archivo de audio local
    val imageUrl: String?,    // Ilustración de la historia
    val narratorName: String, // El sabio que cuenta la historia
    val community: String     // Comunidad de origen (ej. Soledad Atzompa)
)
