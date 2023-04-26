package com.jermaine.dailyfocus.feature.addtask

import com.jermaine.dailyfocus.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.update
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class AddTaskViewModel @Inject constructor(
    interactor: AddTaskInteractor,
) : BaseViewModel<AddTaskAction, AddTaskResult, AddTaskUiState, AddTaskUiEvent>(interactor) {
    override val defaultState: AddTaskUiState
        get() = AddTaskUiState(
            events = null,
            todo = null,
        )

    override fun stateReducer(): (AddTaskUiState, AddTaskResult) -> AddTaskUiState =
        { prevState, result ->
            when (result) {
                is AddTaskResult.LoadTodoSuccessful -> prevState.copy(
                    todo = result.todo,
                )

                AddTaskResult.SaveTodoComplete -> prevState.copy(
                    events = prevState.events?.toMutableList()?.apply {
                        add(AddTaskUiEvent.SaveComplete)
                    } ?: listOf(AddTaskUiEvent.SaveComplete),
                )

                AddTaskResult.DeleteSuccessful -> prevState.copy(
                    events = prevState.events?.toMutableList()?.apply {
                        add(AddTaskUiEvent.DeleteSuccessful)
                    } ?: listOf(AddTaskUiEvent.DeleteSuccessful),
                )
            }
        }

    fun loadTodo(id: UUID) {
        postAction(AddTaskAction.LoadTodo(id))
    }

    fun saveTodo(id: UUID?, title: String, due: LocalTime) {
        postAction(
            AddTaskAction.SaveTodo(
                id = id,
                title = title,
                due = due,
            ),
        )
    }

    fun completeTodo(id: UUID) {
        postAction(
            AddTaskAction.CompleteTodo(id),
        )
    }

    fun deleteTodo(id: UUID) {
        postAction(
            AddTaskAction.DeleteTodo(id),
        )
    }

    override fun consumeEvent(uiEvent: AddTaskUiEvent) {
        uiState.update {
            it.copy(
                events = it.events?.filterNot { event -> event.id == uiEvent.id },
            )
        }
    }
}
