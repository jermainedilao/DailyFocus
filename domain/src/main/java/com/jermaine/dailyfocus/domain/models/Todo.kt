package com.jermaine.dailyfocus.domain.models

import java.time.LocalTime

data class Todo(
    val id: Int,
    val title: String,
    val due: LocalTime,
    val completed: Boolean,
)