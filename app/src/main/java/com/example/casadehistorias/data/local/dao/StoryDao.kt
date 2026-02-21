package com.example.casadehistorias.data.local.dao

import androidx.room.*
import com.example.casadehistorias.data.local.entities.StoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {
    @Query("SELECT * FROM stories")
    fun getAllStories(): Flow<List<StoryEntity>>

    @Query("SELECT * FROM stories WHERE id = :id")
    suspend fun getStoryById(id: String): StoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<StoryEntity>)

    @Delete
    suspend fun deleteStory(story: StoryEntity)
}
