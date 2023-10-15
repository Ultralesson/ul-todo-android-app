package com.example.ul_todo_android_app.date

import com.example.ul_todo_android_app.utilities.DateUtilities
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class DateUtilsTest {

    companion object {
        private val DATE_FORMAT = "dd/MM/yyyy"
        private val MILLI_SECS_IN_A_DAY = 86_400_000L
    }

    @Test
    fun testConvertStringToDateFormat() {
        // Arrange
        val inputDate = "4/2/2002"
        val expectedDate = SimpleDateFormat(DATE_FORMAT).parse(inputDate)

        // Act
        val actualDate = DateUtilities.convertStringToDateFormat(inputDate)

        // Assert
        assertEquals("String to date conversion is incorrectly done", expectedDate, actualDate)
    }

    @Test
    fun testGetCurrentDate() {
        // Arrange
        val expectedDate = SimpleDateFormat(DATE_FORMAT).format(Calendar.getInstance().time)

        // Act
        val actualDate = DateUtilities.getCurrentDate()

        // Assert
        assertEquals("Current date / today's date is incorrect", expectedDate, actualDate)
    }

    @Test
    fun testCalculateDateOffset() {
        // Arrange
        val numberOfDays = 10
        val expectedDate =
            SimpleDateFormat(DATE_FORMAT).format(Date().time + (numberOfDays * MILLI_SECS_IN_A_DAY))

        // Act
        val actualDate = DateUtilities.calculateDateOffset(DATE_FORMAT, 10)

        // Assert
        assertEquals("Calculated offset is wrong", expectedDate, actualDate)
    }

    @Test
    fun testGetDaysInBetweenForStringParameters() {
        // Arrange
        val startDate = "1/1/1970"
        val endDate = "3/1/1970"

        // Act
        val daysInBetween = DateUtilities.getDaysBetween(endDate, startDate)

        // Assert
        assertEquals(
            "The calculated number of days between $startDate and $endDate is incorrect",
            2,
            daysInBetween
        )
    }

    @Test
    fun testGetDaysInBetweenForDateParameters() {
        // Arrange
        val startDate = DateUtilities.convertStringToDateFormat("1/1/1970")
        val endDate = DateUtilities.convertStringToDateFormat("3/1/1970")

        // Act
        val daysInBetween = DateUtilities.getDaysBetween(endDate, startDate)

        // Assert
        assertEquals(
            "The calculated number of days between $startDate and $endDate is incorrect",
            2,
            daysInBetween
        )
    }
}