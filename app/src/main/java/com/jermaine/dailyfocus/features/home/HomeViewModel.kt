package com.jermaine.dailyfocus.features.home

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.jermaine.dailyfocus.core.BaseViewModel
import com.jermaine.dailyfocus.util.TIME_FORMATTER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    interactor: HomeInteractor
) : BaseViewModel<HomeAction, HomeResult, HomeUiState>(interactor),
    DefaultLifecycleObserver {

    override val defaultState: HomeUiState
        get() = HomeUiState(
            items = emptyList(),
            isLoading = false,
            isFirstOpen = true
        )

    override fun stateReducer(): (HomeUiState, HomeResult) -> HomeUiState =
        { prevState, result ->
            when (result) {
                is HomeResult.TodoListLoaded -> prevState.copy(
                    items = result.todoList.map { item ->
                        TodoUiModel(
                            id = item.id,
                            title = item.title,
                            dueDisplayText = item.due.format(TIME_FORMATTER),
                            completed = item.completed
                        )
                    },
                    isFirstOpen = false,
                    isLoading = false,
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
        postAction(HomeAction.LoadTodoList)
    }

    fun onItemComplete(id: Int) {
        postAction(HomeAction.CompleteItem(id))
    }
}