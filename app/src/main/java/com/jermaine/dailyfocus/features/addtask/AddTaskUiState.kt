package com.jermaine.dailyfocus.features.addtask

data class AddTaskUiState(
    val error: Throwable?
)

sealed class AddTaskAction {

}

sealed class AddTaskResult {

}