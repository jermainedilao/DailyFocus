package com.jermaine.dailyfocus.feature.home

import com.jermaine.dailyfocus.domain.model.TodoModel
import java.util.UUID

data class HomeUiState(
    val items: List<TodoUiModel>,
    val isLoading: Boolean,
    val isFirstOpen: Boolean,
)

sealed class HomeUiEvent(val id: UUID)

sealed class HomeAction {
    object LoadTodoList : HomeAction()

    data class CompleteItem(val id: UUID) : HomeAction()
}

sealed class HomeResult {
    data class TodoListLoaded(
        val todoList: List<TodoModel>
    ) : HomeResult()

    object LoadingStarted : HomeResult()

    object LoadingFinished : HomeResult()
}

data class TodoUiModel(
    val id: UUID,
    val title: String,
    val dueDisplayText: String,
    val completed: Boolean
)