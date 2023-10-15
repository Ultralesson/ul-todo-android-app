package com.example.ul_todo_android_app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ul_todo_android_app.database.dataAccessObject.TasksDAO
import com.example.ul_todo_android_app.database.dataAccessObject.UsersDAO
import com.example.ul_todo_android_app.database.entities.TasksEntity
import com.example.ul_todo_android_app.database.entities.UsersEntity

@Database(entities = [UsersEntity::class, TasksEntity::class], version = 6, exportSchema = false)
@androidx.room.TypeConverters(TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usersDAO(): UsersDAO
    abstract fun tasksDAO(): TasksDAO

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    if (System.getProperty("TEST_DB") != "true") {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "todo"
                        ).fallbackToDestructiveMigration().build()
                    } else {
                        instance = Room.inMemoryDatabaseBuilder(
                            context.applicationContext, AppDatabase::class.java
                        ).allowMainThreadQueries().build()
                    }
                }
            }
            return instance!!
        }

        fun destroyInstance() {
            instance = null
        }

        fun closeDB() {
            instance?.close()
        }
    }
}