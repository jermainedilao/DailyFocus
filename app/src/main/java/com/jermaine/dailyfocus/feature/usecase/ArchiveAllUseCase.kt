package com.jermaine.dailyfocus.feature.usecase

import com.jermaine.dailyfocus.data.TodoRepository
import javax.inject.Inject

class ArchiveAllUseCase @Inject constructor(
    private val todoRepository: TodoRepository,
) {
    suspend fun execute() {
        todoRepository.archiveAll()
    }
}
