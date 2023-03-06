package com.jermaine.dailyfocus.core

import kotlinx.coroutines.flow.Flow

interface Interactor<Action, Result> {
    fun actionProcessor(actions: Flow<Action>): Flow<Result>
}
