package com.example.casadehistorias.data.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class StoryFirestore(
    @DocumentId
    val id: String = "",
    val titleEs: String = "",
    val titleNahuatl: String = "",
    val contentEs: String = "",
    val contentNahuatl: String = "",
    val audioUrl: String? = null,
    val imageUrl: String? = null,
    val narratorName: String = "",
    val community: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now(),
    var published: Boolean = true,
    val authorId: String = "",
    val tags: List<String> = emptyList(),
    val latitude: Double? = null,
    val longitude: Double? = null
) {
    constructor() : this(
        id = "",
        titleEs = "",
        titleNahuatl = "",
        contentEs = "",
        contentNahuatl = "",
        audioUrl = null,
        imageUrl = null,
        narratorName = "",
        community = "",
        createdAt = Timestamp.now(),
        updatedAt = Timestamp.now(),
        published = true,
        authorId = "",
        tags = emptyList(),
        latitude = null,
        longitude = null
    )
}
