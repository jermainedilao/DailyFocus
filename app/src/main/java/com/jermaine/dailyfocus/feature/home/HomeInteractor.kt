package com.jermaine.dailyfocus.feature.home

import com.jermaine.dailyfocus.core.Interactor
import com.jermaine.dailyfocus.data.TodoRepository
import com.jermaine.dailyfocus.feature.usecase.ToggleCompleteTodoUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@ExperimentalCoroutinesApi
class HomeInteractor @Inject constructor(
    private val todoRepository: TodoRepository,
    private val toggleCompleteTodoUseCase: ToggleCompleteTodoUseCase,
) : Interactor<HomeAction, HomeResult> {

    private fun subscribeToTodoList(action: HomeAction.LoadTodoList): Flow<HomeResult> {
        return todoRepository
            .observeAll()
            .map(HomeResult::TodoListLoaded)
            .onStart<HomeResult> {
                emit(HomeResult.LoadingStarted)
            }
    }

    private fun toggleCompletion(action: HomeAction.CompleteItem): Flow<HomeResult> {
        return flow {
            toggleCompleteTodoUseCase.execute(action.id)
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