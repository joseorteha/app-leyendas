package com.example.casadehistorias.data.firebase

import com.example.casadehistorias.data.firebase.model.StoryFirestore
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun getAllStoriesFlow(): Flow<List<StoryFirestore>> = callbackFlow {
        val listener = firestore.collection("stories")
            .whereEqualTo("published", true) // Cambiado: antes decía 'isPublished'
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val stories = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(StoryFirestore::class.java)
                } ?: emptyList()

                trySend(stories)
            }

        awaitClose { listener.remove() }
    }

    suspend fun getAllStories(): List<StoryFirestore> {
        return try {
            val snapshot = firestore.collection("stories")
                .whereEqualTo("published", true) // Cambiado: antes decía 'isPublished'
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(StoryFirestore::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getStoryById(id: String): StoryFirestore? {
        return try {
            val document = firestore.collection("stories").document(id).get().await()
            document.toObject(StoryFirestore::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getStoriesByCommmunity(community: String): List<StoryFirestore> {
        return try {
            val snapshot = firestore.collection("stories")
                .whereEqualTo("community", community)
                .whereEqualTo("published", true) // Cambiado
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(StoryFirestore::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addStory(story: StoryFirestore): String? {
        return try {
            val documentRef = firestore.collection("stories").document()
            val storyWithId = story.copy(
                id = documentRef.id,
                createdAt = Timestamp.now(),
                updatedAt = Timestamp.now()
            )
            documentRef.set(storyWithId).await()
            documentRef.id
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateStory(story: StoryFirestore): Boolean {
        return try {
            firestore.collection("stories")
                .document(story.id)
                .set(story.copy(updatedAt = Timestamp.now()))
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteStory(storyId: String): Boolean {
        return try {
            firestore.collection("stories").document(storyId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun searchStories(query: String): List<StoryFirestore> {
        return try {
            val snapshot = firestore.collection("stories")
                .whereEqualTo("published", true) // Cambiado
                .orderBy("titleEs")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(StoryFirestore::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addToFavorites(userId: String, storyId: String): Boolean {
        return try {
            firestore.collection("users").document(userId)
                .update("favoriteStories", com.google.firebase.firestore.FieldValue.arrayUnion(storyId))
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun removeFromFavorites(userId: String, storyId: String): Boolean {
        return try {
            firestore.collection("users").document(userId)
                .update("favoriteStories", com.google.firebase.firestore.FieldValue.arrayRemove(storyId))
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
