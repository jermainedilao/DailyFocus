package com.jermaine.dailyfocus.feature.addtask

import com.jermaine.dailyfocus.core.Interactor
import com.jermaine.dailyfocus.data.TodoRepository
import com.jermaine.dailyfocus.feature.usecase.AddTodoUseCase
import com.jermaine.dailyfocus.feature.usecase.DeleteTodoUseCase
import com.jermaine.dailyfocus.feature.usecase.ToggleCompleteTodoUseCase
import com.jermaine.dailyfocus.feature.usecase.UpdateTodoUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class AddTaskInteractor @Inject constructor(
    private val todoRepository: TodoRepository,
    private val addTodoUseCase: AddTodoUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    private val toggleCompleteTodoUseCase: ToggleCompleteTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
) : Interactor<AddTaskAction, AddTaskResult> {

    private fun loadTodo(action: AddTaskAction.LoadTodo): Flow<AddTaskResult> {
        return todoRepository.observeSingle(action.id)
            .map(AddTaskResult::LoadTodoSuccessful)
    }

    private fun saveTodo(action: AddTaskAction.SaveTodo): Flow<AddTaskResult> {
        return flow {
            if (action.id == null) {
                addTodoUseCase.execute(
                    title = action.title,
                    due = action.due
                )
            } else {
                updateTodoUseCase.execute(
                    id = action.id,
                    title = action.title,
                    due = action.due
                )
            }
            emit(AddTaskResult.SaveTodoComplete)
        }
    }

    private fun completeTodo(action: AddTaskAction.CompleteTodo): Flow<AddTaskResult> {
        return flow {
            toggleCompleteTodoUseCase.execute(action.id)
        }
    }

    private fun deleteTodo(action: AddTaskAction.DeleteTodo): Flow<AddTaskResult> {
        return flow {
            deleteTodoUseCase.execute(action.id)
            emit(AddTaskResult.DeleteSuccessful)
        }
    }

    override fun actionProcessor(actions: Flow<AddTaskAction>): Flow<AddTaskResult> {
        return merge(
            actions.filterIsInstance<AddTaskAction.LoadTodo>()
                .flatMapLatest(::loadTodo),
            actions.filterIsInstance<AddTaskAction.SaveTodo>()
                .flatMapLatest(::saveTodo),
            actions.filterIsInstance<AddTaskAction.CompleteTodo>()
                .flatMapConcat(::completeTodo),
            actions.filterIsInstance<AddTaskAction.DeleteTodo>()
                .flatMapLatest(::deleteTodo)
        )
    }
}