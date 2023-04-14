package com.jermaine.dailyfocus.feature.usecase

import com.jermaine.dailyfocus.data.TodoRepository
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject

class DeleteTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    suspend fun execute(id: UUID) {
        todoRepository.deleteTodo(id)
    }
}