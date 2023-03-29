package com.jermaine.dailyfocus.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch

abstract class BaseViewModel<Action, Result, UiState> constructor(
    interactor: Interactor<Action, Result>
) : ViewModel() {

    protected abstract val defaultState: UiState

    protected abstract fun stateReducer(): (UiState, Result) -> UiState

    private val sharedFlow =
        MutableSharedFlow<Action>(replay = 1, extraBufferCapacity = 64)

    val uiState: MutableStateFlow<UiState> = MutableStateFlow(defaultState)

    init {
        viewModelScope.launch {
            sharedFlow
                .let { interactor.actionProcessor(it) }
                .scan(defaultState) { state, result -> stateReducer().invoke(state, result) }
                .distinctUntilChanged()
                .collect {
                    uiState.value = it
                }
        }
    }

    fun postAction(action: Action) {
        sharedFlow.tryEmit(action)
    }
}
