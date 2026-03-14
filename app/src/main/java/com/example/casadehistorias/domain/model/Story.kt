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
    val latitude: Double? = null,
    val longitude: Double? = null,
    val authorId: String = "",
    val tags: List<String> = emptyList(),
    val createdAt: Long = 0L
)
