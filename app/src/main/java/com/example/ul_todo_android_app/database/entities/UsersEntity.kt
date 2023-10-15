package com.example.ul_todo_android_app.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users", indices = [Index(value = ["userEmail"], unique = true)])
data class UsersEntity(
    @PrimaryKey(autoGenerate = true) var userId: Int? = null,
    var userEmail: String,
    var password: String
) {}