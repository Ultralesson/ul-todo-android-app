package com.example.ul_todo_android_app.database.dataAccessObject

import androidx.room.*
import com.example.ul_todo_android_app.database.entities.TasksEntity

@Dao
interface TasksDAO {
    @Insert
    fun insertTask(task: TasksEntity)

    @Query("UPDATE tasks SET task = :newTask, description = :newDescription, done = :newDone, inProgress = :newInProgress, completionDate = :newCompletionDate, tags = :newTags WHERE userRefEmail = :userRefEmail AND taskId = :taskId")
    fun updateTaskByEmail(
        taskId: Int,
        userRefEmail: String,
        newTask: String,
        newDescription: String,
        newDone: Boolean,
        newInProgress: Boolean,
        newCompletionDate: String,
        newTags: List<String>
    )

    @Query("DELETE FROM tasks WHERE task = :task and userRefEmail = :email")
    fun deleteTask(email: String, task: String)

    @Query("UPDATE tasks SET task = :newTask WHERE userRefEmail = :email AND task = :oldTask")
    fun updateTask(
        email: String,
        oldTask: String,
        newTask: String
    )

    @Query("UPDATE tasks SET description = :description WHERE userRefEmail = :email AND task = :task")
    fun updateTaskDescription(email: String, task: String, description: String)

    @Query("UPDATE tasks SET done = :done WHERE userRefEmail = :email AND task = :task")
    fun updateTaskDoneStatusByEmail(email: String, task: String, done: Boolean)

    @Query("UPDATE tasks SET inProgress = :inProgress WHERE userRefEmail = :userEmail AND task = :task")
    fun updateTaskInProgressStatus(userEmail: String, task: String, inProgress: Boolean)

    @Query("UPDATE tasks SET completionDate = :completionDate WHERE userRefEmail = :userEmail AND task = :task")
    fun updateTaskCompletionDate(userEmail: String, task: String, completionDate: String)

    @Query("UPDATE tasks SET tags = :tags WHERE userRefEmail = :email AND task = :task")
    fun updateTaskTags(email: String, task: String, tags: List<String>)
}