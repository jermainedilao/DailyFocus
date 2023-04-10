package com.jermaine.dailyfocus.data

import com.jermaine.dailyfocus.domain.models.Todo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor() {

    private val items = MutableStateFlow(
//        mapOf(
//            1 to Todo(
//                id = 1,
//                title = "Wash the dishes",
//                due = LocalDateTime.now(),
//                completed = true,
//            ),
//            2 to Todo(
//                id = 2,
//                title = "Do laundry",
//                due = LocalDateTime.now(),
//                completed = false,
//            ),
//            3 to Todo(
//                id = 3,
//                title = "Walk the dog",
//                due = LocalDateTime.now(),
//                completed = false,
//            )
//        )
        emptyMap<Int, Todo>()
    )

    fun observe(): Flow<List<Todo>> =
        items
            .asStateFlow()
            .onStart { delay(1500) }
            .map { it.values.toList().sortedBy { item -> item.due } }

    fun completeTodo(id: Int) {
        items.value[id]?.let { item ->
            val newItems = mutableMapOf<Int, Todo>().apply {
                putAll(items.value)
            }
            newItems[id] = item.copy(
                completed = item.completed.not()
            )
            items.value = newItems
        }
    }

    fun addTodo(todo: Todo) {
        items.value = mutableMapOf<Int, Todo>().apply {
            putAll(items.value)
            put(todo.id, todo)
        }
    }
}