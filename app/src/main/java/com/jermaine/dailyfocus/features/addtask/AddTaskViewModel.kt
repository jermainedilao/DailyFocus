package com.jermaine.dailyfocus.features.addtask

import com.jermaine.dailyfocus.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    interactor: AddTaskInteractor
) : BaseViewModel<AddTaskAction, AddTaskResult, AddTaskUiState, AddTaskUiEvent>(interactor) {
    override val defaultState: AddTaskUiState
        get() = AddTaskUiState(
            events = null
        )

    override fun stateReducer(): (AddTaskUiState, AddTaskResult) -> AddTaskUiState =
        { prevState, result ->
            when (result) {
                AddTaskResult.SaveTodoComplete -> prevState.copy(
                    events = prevState.events?.toMutableList()?.apply {
                        add(AddTaskUiEvent.SaveComplete)
                    } ?: listOf(AddTaskUiEvent.SaveComplete)
                )
            }
        }

    fun saveTodo(title: String, due: LocalTime) {
        postAction(
            AddTaskAction.SaveTodo(
                title = title,
                due = due
            )
        )
    }

    override fun consumeEvent(uiEvent: AddTaskUiEvent) {
        uiState.update {
            it.copy(
                events = it.events?.filterNot { event -> event.id == uiEvent.id }
            )
        }
    }
}