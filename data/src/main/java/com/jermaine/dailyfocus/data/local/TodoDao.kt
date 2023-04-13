package com.jermaine.dailyfocus.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jermaine.dailyfocus.data.local.model.TodoDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo ORDER BY due asc")
    fun observeAll(): Flow<List<TodoDbModel>>

    @Query("SELECT * FROM todo WHERE uid = :uid")
    suspend fun get(uid: String): TodoDbModel

    @Insert
    suspend fun insert(todo: TodoDbModel)

    @Update
    suspend fun update(todo: TodoDbModel)
}