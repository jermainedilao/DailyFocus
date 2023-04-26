package com.jermaine.dailyfocus.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jermaine.dailyfocus.data.local.model.TodoDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo WHERE isArchived = :isArchives ORDER BY due asc")
    fun observeAll(isArchives: Boolean): Flow<List<TodoDbModel>>

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

    @Query("UPDATE todo SET isArchived = 1 WHERE isArchived = 0")
    suspend fun archiveAll()
}
