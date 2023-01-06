package com.example.android.absensiapp.date

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object MyDate {

    @SuppressLint("ConstantLocale")
    private val formatUtc = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

    fun String.fromTimeStampToDate(): Date? {
        formatUtc.timeZone = TimeZone.getTimeZone("UTC")
        return try {
            formatUtc.parse(this)
        } catch (e: ParseException) {
            null
        }
    }

    fun Date.toCalendar(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar
    }

    fun Date.toDay(): String {
        val outputFormat = SimpleDateFormat("dd", Locale.getDefault())
        return outputFormat.format(this)
    }

    fun Date.toMonth(): String {
        val outputFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        return outputFormat.format(this)
    }

    fun Date.toTime(): String {
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return outputFormat.format(this)
    }

    fun Calendar.toDate(): Date {
        return this.time
    }

    fun getCurrentDateForServer(): String {
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(currentTime)
    }
}