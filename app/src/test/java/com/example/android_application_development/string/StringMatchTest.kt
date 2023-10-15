package com.example.ul_todo_android_app.string

import com.example.ul_todo_android_app.utilities.StringUtilities
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class StringMatchTest(
    private val string1: String?,
    private val string2: String?,
    private val expectedMatchResult: Boolean
) {

    companion object {

        @JvmStatic
        @Parameters(name = "{index}: When the string1 {0} and string2 {1}, the expected should be {2}")
        fun data(): List<Array<Any?>> {
            return listOf(
                arrayOf("hello world", "hello world", true),
                arrayOf("hello world", "how are you?", false),
                arrayOf("hello world", null, false),
                arrayOf(null, "hello world", false),
                arrayOf(null, null, false)
            )
        }
    }

    @Test
    fun testMatchStrings() {
        // Act
        val actualMatchResult = StringUtilities.areStringsMatched(string1, string2)

        // Assert
        assertEquals("Unexpected string match result", expectedMatchResult, actualMatchResult)
    }
}