package com.jermaine.dailyfocus.feature.archives

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.jermaine.dailyfocus.core.BaseViewModel
import com.jermaine.dailyfocus.feature.home.HomeAction
import com.jermaine.dailyfocus.feature.home.HomeResult
import com.jermaine.dailyfocus.feature.home.HomeUiEvent
import com.jermaine.dailyfocus.feature.home.HomeUiState
import com.jermaine.dailyfocus.feature.home.TodoInteractor
import com.jermaine.dailyfocus.feature.home.TodoUiModel
import com.jermaine.dailyfocus.util.TIME_FORMATTER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ArchivesViewModel @Inject constructor(
    interactor: TodoInteractor,
) : BaseViewModel<HomeAction, HomeResult, HomeUiState, HomeUiEvent>(interactor),
    DefaultLifecycleObserver {

    override val defaultState: HomeUiState
        get() = HomeUiState(
            items = emptyList(),
            isLoading = false,
            isFirstOpen = true,
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
                            isComplete = item.isComplete,
                        )
                    },
                    isFirstOpen = false,
                    isLoading = false,
                )

                HomeResult.LoadingFinished -> prevState.copy(
                    isLoading = false,
                )

                HomeResult.LoadingStarted -> prevState.copy(
                    isLoading = true,
                )
            }
        }

    override fun onCreate(owner: LifecycleOwner) {
        postAction(HomeAction.LoadArchives)
    }

    override fun consumeEvent(uiEvent: HomeUiEvent) = Unit
}
