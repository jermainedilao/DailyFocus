package com.jermaine.dailyfocus.domain.model

import java.time.LocalTime
import java.util.UUID

data class Todo(
    val id: UUID,
    val title: String,
    val due: LocalTime,
    val completed: Boolean,
)