package com.example.casadehistorias.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.casadehistorias.data.local.dao.StoryDao
import com.example.casadehistorias.data.local.entities.StoryEntity

@Database(
    entities = [StoryEntity::class], 
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
}
