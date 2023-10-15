package com.example.ul_todo_android_app.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ul_todo_android_app.MainActivity
import com.example.ul_todo_android_app.R
import com.example.ul_todo_android_app.database.AppDatabase
import com.example.ul_todo_android_app.utils.ClickWithDelay
import com.example.ul_todo_android_app.utils.ToolBarTitle
import com.example.ul_todo_android_app.utils.Wait
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterAndLoginActivityTest {

    companion object {
        val TEST_EMAIL = "abc@gmail.com"
        val TEST_PASSWORD = "12345"
        val TEST_CONFIRM_PASSWORD = "12345"

        val TEST_DB = "TEST_DB"
    }

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        // Using this property we can set the In-Memory Database instead of using the DB in disk
        System.setProperty(TEST_DB, "true")
    }

    @After
    fun tearDown() {
        AppDatabase.closeDB()
    }

    @Test
    fun testIfIconLoginAndRegisterUIElementsDisplayed() {
        onView(withId(R.id.icTodo)).check(matches(isDisplayed()))
    }

    @Test
    fun testUserSuccessfullyRegisters() {
        // Act

        // Click on register button on home screen
        // Enter the credentials
        // Click on register button on register screen
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.etvEmail)).perform(
            ClickWithDelay.clickWithDelay, typeText(TEST_EMAIL), closeSoftKeyboard()
        )
        onView(withId(R.id.etvPassword)).perform(
            ClickWithDelay.clickWithDelay, typeText(TEST_PASSWORD), closeSoftKeyboard()
        )
        onView(withId(R.id.etvConfirmPassword)).perform(
            ClickWithDelay.clickWithDelay, typeText(TEST_CONFIRM_PASSWORD), closeSoftKeyboard()
        )
        onView(withId(R.id.btnRegister)).perform(click())

        // Assert
        val title: String = "To Do App"
        onView(withId(R.id.eleToolBar)).check(matches(ToolBarTitle.withToolbarTitle(title)))
    }

    @Test
    fun testClearRegisterFieldsFunctionality() {
        // Arrange
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.etvEmail)).perform(
            ClickWithDelay.clickWithDelay, typeText(TEST_EMAIL), closeSoftKeyboard()
        )
        onView(withId(R.id.etvPassword)).perform(
            ClickWithDelay.clickWithDelay, typeText(TEST_PASSWORD), closeSoftKeyboard()
        )
        onView(withId(R.id.etvConfirmPassword)).perform(
            ClickWithDelay.clickWithDelay, typeText(TEST_CONFIRM_PASSWORD), closeSoftKeyboard()
        )

        onView(withId(R.id.imgCloseEmail)).perform(ClickWithDelay.clickWithDelay)
        onView(withId(R.id.imgClosePassword)).perform(ClickWithDelay.clickWithDelay)
        onView(withId(R.id.imgCloseConfirmPassword)).perform(ClickWithDelay.clickWithDelay)

        // Assert
        onView(withId(R.id.etvEmail)).check(matches(withText("")))
        onView(withId(R.id.etvPassword)).check(matches(withText("")))
        onView(withId(R.id.etvConfirmPassword)).check(matches(withText("")))
    }

    @Test
    fun testErrorMessagesAreDisplayedWhileRegisteringWithEmptyFields() {
        // Act
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.btnRegister)).perform(click())

        // Assert
        onView(withId(R.id.txtErrorEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.txtErrorPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.txtErrorConfirmPassword)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavigationToRegisterActivityIfEmailIsNotRegistered() {
        // Arrange
        val testEmail = "abc@gmail.com"
        val testPassword = "12345"

        // Act
        onView(withId(R.id.btnLogin)).perform(click())
        onView(withId(R.id.etvEmail)).perform(
            ClickWithDelay.clickWithDelay, typeText(testEmail), closeSoftKeyboard()
        )
        onView(withId(R.id.etvPassword)).perform(
            ClickWithDelay.clickWithDelay, typeText(testPassword), closeSoftKeyboard()
        )
        onView(withId(R.id.btnSubmit)).perform(click())

        Wait.forView(R.id.btnRegister)

        // Assert
        val title: String = "Register"
        onView(withId(R.id.toolbarRegister)).check(matches(ToolBarTitle.withToolbarTitle(title)))
    }
}
