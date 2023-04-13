package com.jermaine.dailyfocus.data

import com.jermaine.dailyfocus.data.local.TodoDao
import com.jermaine.dailyfocus.data.local.model.TodoDbModel
import com.jermaine.dailyfocus.domain.model.TodoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    private val todoDao: TodoDao
) {
    fun observe(): Flow<List<TodoModel>> =
        todoDao
            .observeAll()
            .distinctUntilChanged()
            .map { it.map(TodoDbModel::toDomain) }

    suspend fun completeTodo(id: UUID) {
        val todo = todoDao.get(id.toString()).run {
            copy(completed = completed.not())
        }
        todoDao.update(todo)
    }

    suspend fun addTodo(title: String, due: LocalTime) {
        val todo = TodoModel(
            id = UUID.randomUUID(),
            title = title,
            due = due,
            completed = false,
        )
        todoDao.insert(TodoDbModel.fromDomain(todo))
    }
}