package com.jermaine.dailyfocus.feature.usecase

import com.jermaine.dailyfocus.data.TodoRepository
import java.time.LocalTime
import javax.inject.Inject

class AddTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository,
) {
    suspend fun execute(
        title: String,
        due: LocalTime,
    ) {
        todoRepository.addTodo(title, due)
    }
}
