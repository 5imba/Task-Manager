package com.bogleo.taskmanager.common

import android.widget.DatePicker
import android.widget.TimePicker
import java.util.*

object TextUtils {

    fun makeTagString(tags: List<String>): String = tags.joinToString(", ")

    fun makeTagList(tagsStr: String): List<String> {
        val tagList = tagsStr.split(',')
        return tagList.filter { tag -> tag.isNotEmpty() }.map { tag -> tag.trim() }
    }

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