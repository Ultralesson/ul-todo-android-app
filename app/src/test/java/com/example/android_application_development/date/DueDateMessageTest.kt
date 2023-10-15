package com.example.ul_todo_android_app.date

import com.example.ul_todo_android_app.utilities.DateUtilities
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.util.*

@RunWith(Parameterized::class)
class DueDateMessageTest(private val date: String, private val expectedDueDateMessage: String) {

    companion object {
        // Due date text messages
        private const val TASK_EXPIRED_MESSAGE = "Task expired"
        private const val TASK_HAPPENING_TODAY_MESSAGE = "Happening today!!!"
        private const val TASK_HAPPENING_TOMORROW = "Due tomorrow!!!"
        private const val TASK_HAPPENING_IN_FEW_DAYS = "Due in %s days"

        // Arrange
        @JvmStatic
        @Parameters(name = "{index}: When the date is {0}, the expected should be - {1}")
        fun data(): List<Array<Any>> {
            val DATE_FORMAT = "dd/MM/yyyy"

            val previousDate = DateUtilities.calculateDateOffset(DATE_FORMAT, -1)
            val currentDate = DateUtilities.calculateDateOffset(DATE_FORMAT, 0)
            val tomorrowDate = DateUtilities.calculateDateOffset(DATE_FORMAT, 1)
            val futureDate = DateUtilities.calculateDateOffset(DATE_FORMAT, 10)

            return listOf(
                arrayOf(previousDate, TASK_EXPIRED_MESSAGE),
                arrayOf(currentDate, TASK_HAPPENING_TODAY_MESSAGE),
                arrayOf(tomorrowDate, TASK_HAPPENING_TOMORROW),
                arrayOf(
                    futureDate, TASK_HAPPENING_IN_FEW_DAYS.format(
                        DateUtilities.getDaysBetween(
                            futureDate, currentDate
                        )
                    )
                )
            )
        }
    }

    @Test
    fun testDueDateMessageForExpiredMessage() {
        // Act
        val actualDueDateMessage = DateUtilities.getDueDateMessage(date)

        // Assert
        assertEquals(
            "Due message is wrong", expectedDueDateMessage, actualDueDateMessage
        )
    }
}