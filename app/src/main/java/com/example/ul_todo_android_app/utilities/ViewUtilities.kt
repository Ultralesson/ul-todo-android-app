package com.example.ul_todo_android_app.utilities

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class ViewUtilities : AppCompatActivity() {
    companion object {

        fun clearEditFields(editText: EditText) {
            editText!!.text.clear()
        }

        fun getTextFromTextFields(textField: TextView): String {
            return textField!!.text.toString()
        }

        fun View.hideKeyboard() {
            val inputManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(windowToken, 0)

        }

        fun textWatcher(viewElement: View, relativeElement: View, method: String) {
            fun methodOperations(view: View) {
                when (method) {
                    "resetText" -> {
                        (view as TextView)!!.text = ""
                    }
                }
            }

            val watcher = object : TextWatcher {
                override fun onTextChanged(
                    sequence: CharSequence?, start: Int, before: Int, count: Int
                ) {
                    methodOperations(relativeElement)
                    // Once the text field is set we are going to make the error message visibility to GONE
                    relativeElement.visibility = View.GONE
                }

                override fun afterTextChanged(sequence: Editable?) {
                    // EMPTY RETURN
                    return
                }

                override fun beforeTextChanged(
                    sequence: CharSequence?, start: Int, before: Int, count: Int
                ) {
                    // EMPTY RETURN
                    return
                }

            }
            (viewElement as EditText).addTextChangedListener(watcher)
        }

        fun simulateProgressBar(viewElement: ProgressBar, start: Int = 2, end: Int = 10) {
            val random = Random()
            val randomNumber = random.nextInt(end - start + 1) + start

            val handler = Handler(Looper.getMainLooper())
            viewElement.visibility = View.VISIBLE

            handler.postDelayed({
                viewElement.visibility = View.GONE
            }, randomNumber.toLong() * 1000)
        }
    }
}