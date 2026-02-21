package com.example.casadehistorias.domain.model

data class Story(
    val id: String,
    val titleEs: String,
    val titleNahuatl: String,
    val contentEs: String,
    val contentNahuatl: String,
    val audioUrl: String?,
    val imageUrl: String?,
    val narratorName: String,
    val community: String,
    val latitude: Double? = null,  // Ubicación opcional
    val longitude: Double? = null  // Ubicación opcional
)
