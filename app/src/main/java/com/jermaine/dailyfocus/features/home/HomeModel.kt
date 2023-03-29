package com.jermaine.dailyfocus.features.home

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.jermaine.dailyfocus.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class HomeModel @Inject constructor(
    interactor: HomeInteractor
) : BaseViewModel<HomeAction, HomeResult, HomeUiState>(interactor),
    DefaultLifecycleObserver {
    override val defaultState: HomeUiState
        get() = HomeUiState(
            todoList = emptyList(),
            isLoading = false,
            isFirstOpen = true
        )

    override fun stateReducer(): (HomeUiState, HomeResult) -> HomeUiState =
        { prevState, result ->
            when (result) {
                is HomeResult.TodoListLoaded -> prevState.copy(
                    todoList = result.todoList.map { item ->
                        TodoUiModel(
                            id = item.id,
                            title = item.title,
                            dueDisplayText = item.due.format(
                                DateTimeFormatter.ofPattern("h:mm a")
                            )
                        )
                    },
                    isFirstOpen = false,
                )
                HomeResult.LoadingFinished -> prevState.copy(
                    isLoading = false
                )
                HomeResult.LoadingStarted -> prevState.copy(
                    isLoading = true
                )
            }
        }

    override fun onCreate(owner: LifecycleOwner) {
        viewModelScope.launch {
            postAction(HomeAction.LoadTodoList)
        }
    }
}