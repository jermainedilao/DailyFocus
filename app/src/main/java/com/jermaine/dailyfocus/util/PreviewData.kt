package com.jermaine.dailyfocus.util

import com.jermaine.dailyfocus.feature.home.TodoUiModel
import java.util.UUID

fun todoUiModelPreviewData() = listOf(
    TodoUiModel(
        id = UUID.randomUUID(),
        title = "Wash the dishes",
        dueDisplayText = "9:00 AM",
        isComplete = true,
    ),
    TodoUiModel(
        id = UUID.randomUUID(),
        title = "Do laundry",
        dueDisplayText = "9:00 AM",
        isComplete = false,
    ),
    TodoUiModel(
        id = UUID.randomUUID(),
        title = "Walk the dog",
        dueDisplayText = "9:00 AM",
        isComplete = false,
    ),
)
