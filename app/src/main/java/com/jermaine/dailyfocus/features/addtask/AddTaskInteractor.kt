package com.jermaine.dailyfocus.features.addtask

import com.jermaine.dailyfocus.core.Interactor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

class AddTaskInteractor @Inject constructor(

) : Interactor<AddTaskAction, AddTaskResult> {
    override fun actionProcessor(actions: Flow<AddTaskAction>): Flow<AddTaskResult> {
        return merge()
    }
}