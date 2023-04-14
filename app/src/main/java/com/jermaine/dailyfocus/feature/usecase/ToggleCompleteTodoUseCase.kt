package com.jermaine.dailyfocus.feature.usecase

import com.jermaine.dailyfocus.data.TodoRepository
import java.util.UUID
import javax.inject.Inject

class ToggleCompleteTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    suspend fun execute(id: UUID) {
        val todo = todoRepository.getTodo(id).let {
            it.copy(
                isComplete = it.isComplete.not()
            )
        }
        todoRepository.updateTodo(todo)
    }
}