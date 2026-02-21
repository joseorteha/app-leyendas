package com.example.casadehistorias.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stories")
data class StoryEntity(
    @PrimaryKey val id: String,
    val titleEs: String,
    val titleNahuatl: String,
    val contentEs: String,
    val contentNahuatl: String,
    val audioPath: String?, // Ruta local al archivo de audio
    val imagePath: String?, // Ruta local a la imagen
    val narratorName: String,
    val community: String,
    val isFavorite: Boolean = false
)
