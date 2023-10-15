package com.example.ul_todo_android_app.utilities

import android.content.Context
import android.content.SharedPreferences

class SessionData(private val context: Context) {

    companion object {
        private const val PREFERENCES_FILE_NAME = "TODO_SESSION"
    }

    fun saveToSession(
        key: String, value: String, customSharedPreferences: SharedPreferences? = null
    ) {
        val session = customSharedPreferences ?: context.getSharedPreferences(
            PREFERENCES_FILE_NAME, Context.MODE_PRIVATE
        )
        session?.edit()?.putString(key, value)?.apply()
    }

    fun getFromSession(key: String, customSharedPreferences: SharedPreferences? = null): String {
        val session = customSharedPreferences ?: context.getSharedPreferences(
            PREFERENCES_FILE_NAME, Context.MODE_PRIVATE
        )
        return session?.getString(key, null)!!
    }
}