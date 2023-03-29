package com.jermaine.dailyfocus.features.home

import com.jermaine.dailyfocus.core.Interactor
import com.jermaine.dailyfocus.domain.models.Todo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import javax.inject.Inject

@ExperimentalCoroutinesApi
class HomeInteractor @Inject constructor() : Interactor<HomeAction, HomeResult> {

    private fun subscribeToTodoList(action: HomeAction.LoadTodoList): Flow<HomeResult> {
        return flow {
            delay(1500)
            emit(
                HomeResult.TodoListLoaded(
                    listOf(
                        Todo(
                            id = 1,
                            title = "Wash the dishes",
                            due = LocalDateTime.now().plusDays(3)
                        ),
                        Todo(
                            id = 2,
                            title = "Do laundry",
                            due = LocalDateTime.now().plusDays(3)
                        ),
                        Todo(
                            id = 3,
                            title = "Walk the dog",
                            due = LocalDateTime.now().plusDays(3)
                        )
                    )
                )
            )
        }.onStart<HomeResult> {
            emit(HomeResult.LoadingStarted)
        }.onCompletion {
            emit(HomeResult.LoadingFinished)
        }
    }

    override fun actionProcessor(actions: Flow<HomeAction>): Flow<HomeResult> {
        return merge(
            actions.filterIsInstance<HomeAction.LoadTodoList>()
                .flatMapLatest(::subscribeToTodoList),
        )
    }
}