package com.example.ul_todo_android_app.utils

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

class ToolBarTitle {

    companion object {
        fun withToolbarTitle(expectedTitle: String): Matcher<View> {
            return object : BoundedMatcher<View, Toolbar>(Toolbar::class.java) {
                override fun describeTo(description: Description?) {
                    description?.appendText("with toolbar title: $expectedTitle")
                }

                override fun matchesSafely(toolBar: Toolbar?): Boolean {
                    return toolBar?.title?.toString() == expectedTitle
                }
            }
        }
    }

}