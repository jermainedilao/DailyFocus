package com.jermaine.dailyfocus.feature.addtask

import java.time.LocalTime
import java.util.UUID

data class AddTaskUiState(
    val events: List<AddTaskUiEvent>?,
)

sealed class AddTaskUiEvent(val id: UUID) {
    object SaveComplete : AddTaskUiEvent(UUID.randomUUID())

    data class Error(val error: Throwable) : AddTaskUiEvent(UUID.randomUUID())
}

sealed class AddTaskAction {
    data class SaveTodo(val title: String, val due: LocalTime) : AddTaskAction()

}

sealed class AddTaskResult {
    object SaveTodoComplete : AddTaskResult()

}