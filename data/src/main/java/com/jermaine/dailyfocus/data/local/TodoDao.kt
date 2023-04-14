package com.jermaine.dailyfocus.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jermaine.dailyfocus.data.local.model.TodoDbModel
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo ORDER BY due asc")
    fun observeAll(): Flow<List<TodoDbModel>>

    @Query("SELECT * FROM todo WHERE uid = :uid")
    suspend fun get(uid: String): TodoDbModel

    @Query("SELECT * FROM todo WHERE uid = :uid")
    fun observeSingle(uid: String): Flow<TodoDbModel>

    @Insert
    suspend fun insert(todo: TodoDbModel)

    @Update
    suspend fun update(todo: TodoDbModel)

    @Query("DELETE FROM todo WHERE uid = :uid")
    suspend fun delete(uid: String)
}