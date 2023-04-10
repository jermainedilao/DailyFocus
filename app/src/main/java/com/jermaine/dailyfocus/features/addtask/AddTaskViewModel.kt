package com.jermaine.dailyfocus.features.addtask

import com.jermaine.dailyfocus.core.BaseViewModel
import javax.inject.Inject

class AddTaskViewModel @Inject constructor(
    interactor: AddTaskInteractor
) : BaseViewModel<AddTaskAction, AddTaskResult, AddTaskUiState>(interactor) {
    override val defaultState: AddTaskUiState
        get() = AddTaskUiState(
            error = null
        )

    override fun stateReducer(): (AddTaskUiState, AddTaskResult) -> AddTaskUiState =
        { prevState, result ->
            prevState
        }
}