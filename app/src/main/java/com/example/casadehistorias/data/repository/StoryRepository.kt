package com.example.casadehistorias.data.repository

import com.example.casadehistorias.data.firebase.AuthRepository
import com.example.casadehistorias.data.firebase.FirestoreRepository
import com.example.casadehistorias.data.firebase.model.StoryFirestore
import com.example.casadehistorias.domain.model.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepository @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val authRepository: AuthRepository
) {

    fun getAllStoriesFlow(): Flow<List<Story>> =
        firestoreRepository.getAllStoriesFlow().map { stories ->
            stories.map { it.toDomainModel() }
        }

    suspend fun getAllStories(): List<Story> {
        return firestoreRepository.getAllStories().map { it.toDomainModel() }
    }

    suspend fun getStoryById(id: String): Story? {
        return firestoreRepository.getStoryById(id)?.toDomainModel()
    }

    suspend fun getStoriesByCommunity(community: String): List<Story> {
        return firestoreRepository.getStoriesByCommunity(community).map { it.toDomainModel() }
    }

    suspend fun getStoriesByAuthor(authorId: String): List<Story> {
        return firestoreRepository.getStoriesByAuthor(authorId).map { it.toDomainModel() }
    }

    suspend fun getMyStories(): List<Story> {
        val currentUser = authRepository.currentUser ?: return emptyList()
        return getStoriesByAuthor(currentUser.uid)
    }

    suspend fun getStoriesByIds(ids: List<String>): List<Story> {
        return firestoreRepository.getStoriesByIds(ids).map { it.toDomainModel() }
    }

    suspend fun searchStories(query: String): List<Story> {
        return firestoreRepository.searchStories(query).map { it.toDomainModel() }
    }

    suspend fun addStory(story: Story): String? {
        val currentUser = authRepository.currentUser
        return if (currentUser != null) {
            val storyFirestore = story.toFirestoreModel(currentUser.uid)
            firestoreRepository.addStory(storyFirestore)
        } else null
    }

    suspend fun updateStory(story: Story): Boolean {
        val currentUser = authRepository.currentUser
        return if (currentUser != null) {
            val storyFirestore = story.toFirestoreModel(currentUser.uid)
            firestoreRepository.updateStory(storyFirestore)
        } else false
    }

    suspend fun deleteStory(storyId: String): Boolean {
        return firestoreRepository.deleteStory(storyId)
    }

    suspend fun addToFavorites(storyId: String): Boolean {
        val currentUser = authRepository.currentUser
        return if (currentUser != null) {
            firestoreRepository.addToFavorites(currentUser.uid, storyId)
        } else false
    }

    suspend fun removeFromFavorites(storyId: String): Boolean {
        val currentUser = authRepository.currentUser
        return if (currentUser != null) {
            firestoreRepository.removeFromFavorites(currentUser.uid, storyId)
        } else false
    }
}

// Extension functions for converting between models
private fun StoryFirestore.toDomainModel(): Story {
    return Story(
        id = this.id,
        titleEs = this.titleEs,
        titleNahuatl = this.titleNahuatl,
        contentEs = this.contentEs,
        contentNahuatl = this.contentNahuatl,
        audioUrl = this.audioUrl,
        imageUrl = this.imageUrl,
        narratorName = this.narratorName,
        community = this.community,
        latitude = this.latitude,
        longitude = this.longitude,
        authorId = this.authorId,
        tags = this.tags,
        createdAt = this.createdAt.seconds * 1000
    )
}

private fun Story.toFirestoreModel(authorId: String): StoryFirestore {
    return StoryFirestore(
        id = this.id,
        titleEs = this.titleEs,
        titleNahuatl = this.titleNahuatl,
        contentEs = this.contentEs,
        contentNahuatl = this.contentNahuatl,
        audioUrl = this.audioUrl,
        imageUrl = this.imageUrl,
        narratorName = this.narratorName,
        community = this.community,
        authorId = authorId,
        published = true,
        latitude = this.latitude,
        longitude = this.longitude,
        tags = this.tags
    )
}
