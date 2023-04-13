package com.jermaine.dailyfocus.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jermaine.dailyfocus.domain.model.TodoModel
import java.time.LocalTime
import java.util.UUID

@Entity(tableName = "todo")
data class TodoDbModel(
    @PrimaryKey val uid: String,
    val title: String,
    val due: LocalTime,
    val completed: Boolean,
) {
    companion object {
        fun toDomain(dbModel: TodoDbModel): TodoModel {
            return with(dbModel) {
                TodoModel(
                    id = UUID.fromString(uid),
                    title = title,
                    due = due,
                    completed = completed
                )
            }
        }

        fun fromDomain(todo: TodoModel): TodoDbModel {
            return with(todo) {
                TodoDbModel(
                    uid = id.toString(),
                    title = title,
                    due = due,
                    completed = completed
                )
            }
        }
    }
}