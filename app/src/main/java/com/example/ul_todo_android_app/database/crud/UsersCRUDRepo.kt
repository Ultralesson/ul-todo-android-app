package com.example.ul_todo_android_app.database.crud

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.os.Handler
import android.os.Looper
import com.example.ul_todo_android_app.database.AppDatabase
import com.example.ul_todo_android_app.database.dataAccessObject.UsersDAO
import com.example.ul_todo_android_app.database.entities.UsersEntity

class UsersCRUDRepo(context: Context) {

    private val usersDAO: UsersDAO? = AppDatabase.getInstance(context).usersDAO()

    fun insertUser(user: UsersEntity, callback: (insertStatus: Boolean) -> Unit) {
        Thread {
            var insertStatus: Boolean = false
            val handler = Handler(Looper.getMainLooper())

            try {
                user.userEmail = user.userEmail.trim()
                usersDAO?.insertUser(user)
                insertStatus = true
            } catch (exception: SQLiteConstraintException) {
                // executed when the duplicate email is about to be inserted
            }
            handler.post {
                callback(insertStatus)
            }
        }.start()
    }

    fun getUserByEmail(email: String, callback: (user: UsersEntity?) -> Unit) {
        Thread {
            val handler = Handler(Looper.getMainLooper())
            val user: UsersEntity? = usersDAO?.getUserByEmail(email)

            handler.post {
                callback(user)
            }
        }.start()
    }

    fun getAllUsers(callback: (List<UsersEntity>) -> Unit) {
        Thread {
            val handler = Handler(Looper.getMainLooper())
            val allUsers = usersDAO?.getAllUsers()!!
            handler.post {
                callback(allUsers)
            }
        }.start()
    }

    fun deleteUserByEmail(userEmail: String) {
        Thread {
            val handle = Handler(Looper.getMainLooper())
            usersDAO?.deleteUserByEmail(userEmail)

            handle.post {
                // logic after delete
            }
        }.start()
    }

    fun updatePassword(userEmail: String, newPassword: String) {
        Thread {
            val handle = Handler(Looper.getMainLooper())
            usersDAO?.updatePassword(userEmail, newPassword)

            handle.post {
                // logic after delete
            }
        }.start()
    }
}