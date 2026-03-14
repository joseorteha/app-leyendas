package com.example.casadehistorias.data.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class UserProfile(
    @DocumentId
    val id: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String? = null,
    val community: String = "",
    val isStoryteller: Boolean = false,
    val favoriteStories: List<String> = emptyList(),
    val createdAt: Timestamp = Timestamp.now(),
    val lastLogin: Timestamp = Timestamp.now()
)
