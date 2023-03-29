package com.jermaine.dailyfocus.features.home

import com.jermaine.dailyfocus.domain.models.Todo

data class HomeUiState(
    val todoList: List<TodoUiModel>,
    val isLoading: Boolean,
    val isFirstOpen: Boolean,
)

sealed class HomeAction {
    object LoadTodoList : HomeAction()
}

sealed class HomeResult {
    data class TodoListLoaded(
        val todoList: List<Todo>
    ) : HomeResult()

    object LoadingStarted : HomeResult()

    object LoadingFinished : HomeResult()
}

data class TodoUiModel(
    val id: Long,
    val title: String,
    val dueDisplayText: String,
)