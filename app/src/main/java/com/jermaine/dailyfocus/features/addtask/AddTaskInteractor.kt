package com.jermaine.dailyfocus.features.addtask

import com.jermaine.dailyfocus.core.Interactor
import com.jermaine.dailyfocus.data.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

class AddTaskInteractor @Inject constructor(
    private val todoRepository: TodoRepository
) : Interactor<AddTaskAction, AddTaskResult> {

    private fun saveTodo(action: AddTaskAction.SaveTodo): Flow<AddTaskResult> {
        return flow {
            todoRepository.addTodo(
                title = action.title,
                due = action.due
            )
            emit(AddTaskResult.SaveTodoComplete)
        }
    }

    override fun actionProcessor(actions: Flow<AddTaskAction>): Flow<AddTaskResult> {
        return merge(
            actions.filterIsInstance<AddTaskAction.SaveTodo>()
                .flatMapLatest(::saveTodo)
        )
    }
}