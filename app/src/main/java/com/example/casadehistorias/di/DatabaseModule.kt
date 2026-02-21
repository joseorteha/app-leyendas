package com.example.casadehistorias.di

import android.content.Context
import androidx.room.Room
import com.example.casadehistorias.data.local.AppDatabase
import com.example.casadehistorias.data.local.dao.StoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "casa_historias_db"
        ).build()
    }

    @Provides
    fun provideStoryDao(database: AppDatabase): StoryDao {
        return database.storyDao()
    }
}
