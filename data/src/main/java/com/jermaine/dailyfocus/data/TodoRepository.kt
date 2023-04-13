package com.jermaine.dailyfocus.data

import com.jermaine.dailyfocus.domain.model.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor() {

    private val items = MutableStateFlow(
        emptyMap<UUID, Todo>()
    )

    fun observe(): Flow<List<Todo>> =
        items
            .asStateFlow()
            .map { it.values.toList().sortedBy { item -> item.due } }

    fun completeTodo(id: UUID) {
        items.value[id]?.let { item ->
            val newItems = mutableMapOf<UUID, Todo>().apply {
                putAll(items.value)
            }
            newItems[id] = item.copy(
                completed = item.completed.not()
            )
            items.value = newItems
        }
    }

    fun addTodo(title: String, due: LocalTime) {
        items.value = mutableMapOf<UUID, Todo>().apply {
            putAll(items.value)

            val todo = Todo(
                id = UUID.randomUUID(),
                title = title,
                due = due,
                completed = false,
            )

            put(todo.id, todo)
        }
    }
}