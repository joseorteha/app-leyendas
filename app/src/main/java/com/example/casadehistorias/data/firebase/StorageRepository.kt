package com.example.casadehistorias.data.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageRepository @Inject constructor(
    private val storage: FirebaseStorage
) {

    suspend fun uploadImage(uri: Uri, storyId: String): String? {
        return try {
            val ref = storage.reference.child("stories/$storyId/cover.jpg")
            ref.putFile(uri).await()
            ref.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun uploadAudio(uri: Uri, storyId: String): String? {
        return try {
            val ref = storage.reference.child("stories/$storyId/audio.mp3")
            ref.putFile(uri).await()
            ref.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deleteFile(url: String): Boolean {
        return try {
            storage.getReferenceFromUrl(url).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
