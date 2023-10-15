package com.example.ul_todo_android_app.utils

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

class ClickWithDelay {
    companion object {
        val clickWithDelay = object : ViewAction {
            override fun getConstraints(): Matcher<View> = ViewMatchers.isClickable()
            override fun getDescription(): String = "click with delay"
            override fun perform(uiController: UiController?, view: View?) {
                uiController?.loopMainThreadForAtLeast(500) // wait for 500ms
                view?.performClick()
            }
        }
    }
}