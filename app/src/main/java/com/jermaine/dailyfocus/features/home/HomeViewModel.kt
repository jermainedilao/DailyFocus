package com.jermaine.dailyfocus.features.home

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.jermaine.dailyfocus.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    interactor: HomeInteractor
) : BaseViewModel<HomeAction, HomeResult, HomeUiState>(interactor),
    DefaultLifecycleObserver {

    private val dateTimeFormatter by lazy {
        DateTimeFormatter.ofPattern("h:mm a")
    }

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
                            dueDisplayText = item.due.format(dateTimeFormatter),
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