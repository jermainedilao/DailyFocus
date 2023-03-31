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

    // TODO: Move this to repository
    private val items = MutableStateFlow(
        mapOf(
            1 to Todo(
                id = 1,
                title = "Wash the dishes",
                due = LocalDateTime.now().plusDays(3),
                completed = true,
            ),
            2 to Todo(
                id = 2,
                title = "Do laundry",
                due = LocalDateTime.now().plusDays(3),
                completed = false,
            ),
            3 to Todo(
                id = 3,
                title = "Walk the dog",
                due = LocalDateTime.now().plusDays(3),
                completed = false,
            )
        )
    )

    private fun subscribeToTodoList(action: HomeAction.LoadTodoList): Flow<HomeResult> {
        return flow {
            delay(1500)
            emit(Unit)
        }.flatMapLatest {
            items.asStateFlow().map {
                HomeResult.TodoListLoaded(
                    it.values.toList()
                )
            }
        }.onStart<HomeResult> {
            emit(HomeResult.LoadingStarted)
        }
    }

    private fun toggleCompletion(action: HomeAction.CompleteItem): Flow<HomeResult> {
        return flow {
            items.value[action.id]?.let { item ->
                val newItems = mutableMapOf<Int, Todo>().apply {
                    putAll(items.value)
                }
                newItems[action.id] = item.copy(
                    completed = item.completed.not()
                )
                items.value = newItems
            }
        }
    }

    override fun actionProcessor(actions: Flow<HomeAction>): Flow<HomeResult> {
        return merge(
            actions.filterIsInstance<HomeAction.LoadTodoList>()
                .flatMapLatest(::subscribeToTodoList),
            actions.filterIsInstance<HomeAction.CompleteItem>()
                .flatMapLatest(::toggleCompletion),
        )
    }
}