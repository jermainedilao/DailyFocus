package com.jermaine.dailyfocus.feature.usecase

import com.jermaine.dailyfocus.data.TodoRepository
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject

class UpdateTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository,
) {
    suspend fun execute(
        id: UUID,
        title: String,
        due: LocalTime,
    ) {
        val todo = todoRepository.getTodo(id)
            .copy(
                title = title,
                due = due,
            )
        todoRepository.updateTodo(todo)
    }
}
