package com.example.ul_todo_android_app.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.ul_todo_android_app.R

class CustomToastUtility(private val context: Context) {

    fun showToast(layoutResId: Int, message: String) {
        // Inflate the custom toast layout
        val inflater = LayoutInflater.from(context)
        val layout: View = inflater.inflate(layoutResId, null)

        // Get a reference to the TextView in the custom toast layout
        val toastTextView: TextView = layout.findViewById(R.id.toastText)

        // Set the message text and mirrored text color
        toastTextView.text = message

        // Create and show the custom toast
        val toast = Toast(context)
        toast.view = layout
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }
}