package com.example.ul_todo_android_app.database.crud

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.ul_todo_android_app.database.AppDatabase
import com.example.ul_todo_android_app.database.dataAccessObject.TasksDAO
import com.example.ul_todo_android_app.database.dataAccessObject.UsersDAO
import com.example.ul_todo_android_app.database.entities.TasksEntity
import com.example.ul_todo_android_app.database.entities.UserWithTasks

class TasksCRUDRepo(context: Context) {
    private val tasksDAO: TasksDAO? =
        AppDatabase.getInstance(context)?.tasksDAO()
    private val usersDAO: UsersDAO? =
        AppDatabase.getInstance(context)?.usersDAO()

    fun getTasksForUser(email: String, callback: (List<UserWithTasks>) -> Unit) {
        Thread {
            val handler = Handler(Looper.getMainLooper())
            val tasks = usersDAO?.getUsersWithTasks(email.trim())!!

            handler.post {
                callback(tasks)
            }
        }.start()
    }

    fun getTask(email: String, taskValue: String, callback: (TasksEntity) -> Unit) {
        Thread {
            val handler = Handler(Looper.getMainLooper())
            val tasks = usersDAO?.getUsersWithTasks(email.trim())
            val task = tasks?.get(0)?.tasks?.find { it.task == taskValue }

            handler.post {
                callback(task!!)
            }
        }.start()
    }

    fun insertTask(task: TasksEntity) {
        Thread {
            val handler = Handler(Looper.getMainLooper())

            val userEmail = task.userRefEmail
            val user = usersDAO?.getUserByEmail(userEmail)
            if (user != null) {
                task.task = task.task.trim()
                task.description = task.description.trim()
                task.completionDate = task.completionDate.trim()
                task.tags = task.tags.map { item -> item.trim() }
                task.userRefEmail = user.userEmail
                tasksDAO?.insertTask(task)
            }

            handler.post {

            }
        }.start()
    }

    fun updateTaskByEmail(task: TasksEntity, taskId: Int) {
        Thread {
            val handler = Handler(Looper.getMainLooper())

            if (taskId != null) {
                tasksDAO?.updateTaskByEmail(
                    taskId = taskId,
                    newTask = task.task,
                    newDescription = task.description,
                    newCompletionDate = task.completionDate,
                    userRefEmail = task.userRefEmail,
                    newTags = task.tags,
                    newDone = task.done,
                    newInProgress = task.inProgress
                )
            }

            handler.post {
                // logic after the update of the task
            }
        }.start()
    }

    fun deleteTask(email: String, task: String) {
        Thread {
            val handler = Handler(Looper.getMainLooper())
            tasksDAO?.deleteTask(email, task)

            handler.post {

            }
        }.start()
    }

    fun updateTask(email: String, oldTask: String, newTask: String) {
        tasksDAO?.updateTask(email, oldTask, newTask)
    }

    fun updateTaskDescription(email: String, task: String, description: String) {
        tasksDAO?.updateTaskDescription(email, task, description)
    }

    fun updateTaskDoneStatus(email: String, task: String, done: Boolean) {
        Thread {
            val handler = Handler(Looper.getMainLooper())
            tasksDAO?.updateTaskDoneStatusByEmail(email, task, done)

            handler.post {

            }
        }.start()
    }

    fun updateTaskInProgressStatus(
        email: String,
        task: String,
        inProgress: Boolean,
        callback: () -> Unit
    ) {
        Thread {
            val handler = Handler(Looper.getMainLooper())
            tasksDAO?.updateTaskInProgressStatus(email, task, inProgress)

            handler.post {
                callback()
            }
        }.start()
    }

    fun updateTaskCompletionDate(email: String, task: String, completionDate: String) {
        tasksDAO?.updateTaskCompletionDate(email, task, completionDate)
    }

    fun updateTaskTags(email: String, task: String, tags: List<String>) {
        tasksDAO?.updateTaskTags(email, task, tags)
    }
}