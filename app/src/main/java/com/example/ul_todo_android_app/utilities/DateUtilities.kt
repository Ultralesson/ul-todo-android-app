package com.example.ul_todo_android_app.utilities

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DateUtilities {

    companion object {
        fun getDueDateMessage(dateString: String): String {
            val inputFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val inputDate = inputFormatter.parse(dateString)

            // Get the difference between the input date and the current date
            val currentDate = convertStringToDateFormat(getCurrentDate())
            val daysLeft = getDaysBetween(inputDate, currentDate).toLong()

            return when {
                daysLeft < 0L -> "Task expired"
                daysLeft == 0L -> "Happening today!!!"
                daysLeft == 1L -> "Due tomorrow!!!"
                else -> "Due in $daysLeft days"
            }
        }

        fun convertStringToDateFormat(date: String, format: String = "dd/MM/yyyy"): Date {
            return SimpleDateFormat(format).parse(date)
        }

        fun getDaysBetween(endDate: Date, startDate: Date): Int {
            return TimeUnit.DAYS.convert(endDate.time - startDate.time, TimeUnit.MILLISECONDS)
                .toInt()
        }

        fun getDaysBetween(endDate: String, startDate: String): Int {
            val endDate_ = convertStringToDateFormat(endDate)
            val startDate_ = convertStringToDateFormat(startDate)
            return getDaysBetween(endDate_, startDate_)
        }

        fun getCurrentDate(): String {
            val currentDate = Date()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val formattedDate = dateFormat.format(currentDate)
            return formattedDate.toString()
        }

        fun calculateDateOffset(dateFormat: String, numberOfDays: Int = 1): String {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, numberOfDays)
            return SimpleDateFormat(dateFormat).format(calendar.time)
        }
    }
}