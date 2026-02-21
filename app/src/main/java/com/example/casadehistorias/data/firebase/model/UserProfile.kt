package com.example.casadehistorias.data.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class UserProfile(
    @DocumentId
    val id: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String? = null,
    val community: String = "", // Comunidad de origen
    val isStoryteller: Boolean = false, // Si puede subir historias
    val favoriteStories: List<String> = emptyList(), // IDs de historias favoritas
    val createdAt: Timestamp = Timestamp.now(),
    val lastLogin: Timestamp = Timestamp.now()
) {
    // Constructor vacío requerido por Firestore
    constructor() : this(
        id = "",
        email = "",
        displayName = "",
        photoUrl = null,
        community = "",
        isStoryteller = false,
        favoriteStories = emptyList(),
        createdAt = Timestamp.now(),
        lastLogin = Timestamp.now()
    )
}
