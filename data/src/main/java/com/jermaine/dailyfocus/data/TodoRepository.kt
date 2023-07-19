package com.jermaine.dailyfocus.data

import com.jermaine.dailyfocus.data.local.TodoDao
import com.jermaine.dailyfocus.data.local.model.TodoDbModel
import com.jermaine.dailyfocus.domain.model.TodoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    private val todoDao: TodoDao,
) {
    fun observeAll(createdAt: LocalDate): Flow<List<TodoModel>> =
        todoDao
            .observeAll(createdAt)
            .distinctUntilChanged()
            .map { it.map(TodoDbModel::toDomain) }

    fun observeSingle(id: UUID): Flow<TodoModel> =
        todoDao
            .observeSingle(id.toString())
            .distinctUntilChanged()
            .filterNotNull()
            .map(TodoDbModel::toDomain)

    suspend fun getTodo(id: UUID): TodoModel {
        return TodoDbModel.toDomain(
            todoDao.get(id.toString()),
        )
    }

    suspend fun addTodo(title: String, due: LocalTime) {
        val todo = TodoModel(
            id = UUID.randomUUID(),
            title = title,
            due = due,
            isComplete = false,
            createdAt = LocalDate.now(),
        )
        todoDao.insert(TodoDbModel.fromDomain(todo))
    }

    suspend fun updateTodo(todo: TodoModel) {
        todoDao.update(TodoDbModel.fromDomain(todo))
    }

    suspend fun deleteTodo(id: UUID) {
        todoDao.delete(id.toString())
    }
}
