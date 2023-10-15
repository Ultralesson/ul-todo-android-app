package com.example.ul_todo_android_app.sessionData

import android.content.Context
import android.content.SharedPreferences
import com.example.ul_todo_android_app.utilities.SessionData
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class SessionDataTest {

    private lateinit var sessionData: SessionData
    private lateinit var mockContext: Context
    private lateinit var mockPreferences: SharedPreferences

    @Before
    fun setUp() {
        mockContext = mock(Context::class.java)
        mockPreferences = mock(SharedPreferences::class.java)

        `when`(mockContext.getSharedPreferences(anyString(), eq(Context.MODE_PRIVATE))).thenReturn(
            mockPreferences
        )

        sessionData = SessionData(mockContext)
    }

    @Test
    fun testSaveAndRetrieveSessionData() {
        val KEY = "key"
        val VALUE = "value"

        // Act
        sessionData.saveToSession(KEY, VALUE, mockPreferences)
        val retrievedValue = sessionData.getFromSession(KEY, mockPreferences)

        // Assert
        assertEquals("Failed to retrieve the value from the session storage", VALUE, retrievedValue)
    }
}