package com.jermaine.dailyfocus.domain.models

import java.time.LocalDateTime

data class Todo(
    val id: Int,
    val title: String,
    val due: LocalDateTime,
    val completed: Boolean,
)