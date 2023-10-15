package com.example.ul_todo_android_app.utils

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class Wait {

    companion object {
        fun forView(viewId: Int) {
            val idlingResource = CountingIdlingResource("waitForView")
            IdlingRegistry.getInstance().register(idlingResource)

            runBlocking {
                while (true) {
                    try {
                        Espresso.onView(ViewMatchers.withId(viewId))
                            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                        break
                    } catch (exception: Exception) {
                        delay(500)
                    }
                }
            }
            IdlingRegistry.getInstance().unregister(idlingResource)
        }
    }
}