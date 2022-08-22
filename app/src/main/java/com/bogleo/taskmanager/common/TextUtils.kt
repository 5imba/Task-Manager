package com.bogleo.taskmanager.common

import android.widget.DatePicker
import android.widget.TimePicker
import java.util.*

object TextUtils {

    fun makeDateTimeText(
        datePicker: DatePicker,
        timePicker: TimePicker
    ): String {

        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year
        val hour = timePicker.hour
        val minute = timePicker.minute
        val is24 = timePicker.is24HourView

        val date = "${"%02d".format(day)}.${"%02d".format(month)}.${year}"
        val time: String = if (is24){
            "${hour}:${minute}"
        } else{
            val newHour = hour % 12
            val amPm = if (hour > 12) "PM" else "AM"
            "${"%02d".format(newHour)}:${"%02d".format(minute)} $amPm"
        }
        return "$date, $time"
    }

    fun makeDateString(datePicker: DatePicker): String {
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year
        return "${"%02d".format(day)}.${"%02d".format(month)}.${year}"
    }

    fun makeTimeString(timePicker: TimePicker): String {
        val hour = timePicker.hour
        val minute = timePicker.minute
        return "${"%02d".format(hour)}:${"%02d".format(minute)}"
    }

    fun getMillisFromDateTime(datePicker: DatePicker, timePicker: TimePicker): Long {

        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year
        val hour = timePicker.hour
        val minute = timePicker.minute

        return getMillisFromDateTime(
            year = year,
            month = month,
            day= day,
            hour = hour,
            minute = minute
        )
    }

    private fun getMillisFromDateTime(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute, 0)
        return calendar.timeInMillis
    }
}