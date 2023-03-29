package com.jermaine.dailyfocus.domain.models

import java.time.LocalDateTime

data class Todo(
    val id: Long,
    val title: String,
    val due: LocalDateTime,
)