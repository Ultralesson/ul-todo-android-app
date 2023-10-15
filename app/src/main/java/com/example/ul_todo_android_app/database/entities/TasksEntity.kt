package com.example.ul_todo_android_app.database.entities

import androidx.room.*

@Entity(
    tableName = "tasks", foreignKeys = [ForeignKey(
        entity = UsersEntity::class,
        parentColumns = ["userEmail"],
        childColumns = ["userRefEmail"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TasksEntity(
    @PrimaryKey(autoGenerate = true) var taskId: Int? = null,
    var task: String,
    var description: String,
    var done: Boolean,
    var inProgress: Boolean,
    var completionDate: String,
    var tags: List<String>,
    @ColumnInfo(name = "userRefEmail", index = true) var userRefEmail: String
)

data class UserWithTasks(
    @Embedded val user: UsersEntity,
    @Relation(
        parentColumn = "userEmail",
        entityColumn = "userRefEmail"
    )
    var tasks: List<TasksEntity> = emptyList()
)
