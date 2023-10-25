package com.example.ul_todo_android_app.utilities

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.ul_todo_android_app.constants.SessionStoreTexts

object SessionManager {
    private var loggedInEmail: String? = null

    fun getLoggedInEmail(context: Context, intent: Intent): String {
        if (loggedInEmail.isNullOrEmpty()) {
            // Fetch the email from the intent or session data as you did before
            val intentEmail = intent.getStringExtra("registeredEmail")
            loggedInEmail = if (intentEmail != null && intentEmail.isNotEmpty()) {
                intentEmail
            } else {
                SessionData(context.applicationContext).getFromSession(SessionStoreTexts.LOGGED_IN_EMAIL)
            }
            Log.i("intent", " $intentEmail")
            Log.i("email", " $loggedInEmail")
        }
        return loggedInEmail ?: ""
    }
}
