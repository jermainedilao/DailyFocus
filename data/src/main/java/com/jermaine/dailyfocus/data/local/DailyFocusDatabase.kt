package com.jermaine.dailyfocus.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jermaine.dailyfocus.data.local.model.TodoDbModel

@TypeConverters(Converters::class)
@Database(entities = [TodoDbModel::class], version = 1)
abstract class DailyFocusDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}