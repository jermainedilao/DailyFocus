package com.jermaine.dailyfocus.di

import android.content.Context
import androidx.room.Room
import com.jermaine.dailyfocus.data.local.DailyFocusDatabase
import com.jermaine.dailyfocus.data.local.TodoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): DailyFocusDatabase {
        return Room.databaseBuilder(
            context,
            DailyFocusDatabase::class.java,
            "daily-focus-database",
        ).build()
    }

    @Singleton
    @Provides
    fun providesTodoDao(database: DailyFocusDatabase): TodoDao {
        return database.todoDao()
    }
}
