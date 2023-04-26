package com.jermaine.dailyfocus.feature.addtask

import com.jermaine.dailyfocus.domain.model.TodoModel
import java.time.LocalTime
import java.util.UUID

data class AddTaskUiState(
    val events: List<AddTaskUiEvent>?,
    val todo: TodoModel?,
)

sealed class AddTaskUiEvent(val id: UUID) {
    object SaveComplete : AddTaskUiEvent(UUID.randomUUID())

    object DeleteSuccessful : AddTaskUiEvent(UUID.randomUUID())

    data class Error(val error: Throwable) : AddTaskUiEvent(UUID.randomUUID())
}

sealed class AddTaskAction {
    data class LoadTodo(val id: UUID) : AddTaskAction()

    data class SaveTodo(val id: UUID?, val title: String, val due: LocalTime) : AddTaskAction()

    data class CompleteTodo(val id: UUID) : AddTaskAction()

    data class DeleteTodo(val id: UUID) : AddTaskAction()
}

sealed class AddTaskResult {
    data class LoadTodoSuccessful(
        val todo: TodoModel,
    ) : AddTaskResult()

    object SaveTodoComplete : AddTaskResult()
    object DeleteSuccessful : AddTaskResult()
}
