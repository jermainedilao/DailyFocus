package com.jermaine.dailyfocus.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jermaine.dailyfocus.data.local.model.TodoDbModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo WHERE createdAt = :createdAt ORDER BY due asc")
    fun observeAll(createdAt: LocalDate): Flow<List<TodoDbModel>>

    @Query("SELECT * FROM todo WHERE createdAt < :now ORDER BY due asc")
    fun observeArchives(now: LocalDate = LocalDate.now()): Flow<List<TodoDbModel>>

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
