package com.example.ul_todo_android_app.database.dataAccessObject

import androidx.room.*
import com.example.ul_todo_android_app.database.entities.UserWithTasks
import com.example.ul_todo_android_app.database.entities.UsersEntity

@Dao
interface UsersDAO {
    @Insert
    fun insertUser(users: UsersEntity)

    @Query("SELECT * FROM users")
    fun getAllUsers(): List<UsersEntity>

    @Query("SELECT * FROM users WHERE userEmail = :email")
    fun getUserByEmail(email: String): UsersEntity?

    @Update
    fun updateUser(users: UsersEntity)

    @Query("DELETE from users WHERE userEmail = :userEmail")
    fun deleteUserByEmail(userEmail: String)

    @Transaction
    @Query("SELECT * FROM users WHERE userEmail = :email")
    fun getUsersWithTasks(email: String): List<UserWithTasks>

    @Query("UPDATE users SET password = :newPassword WHERE userEmail = :userEmail")
    fun updatePassword(userEmail: String, newPassword: String)
}