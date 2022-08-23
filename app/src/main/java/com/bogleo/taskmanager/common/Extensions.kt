package com.bogleo.taskmanager.common

import android.content.res.ColorStateList
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TimePicker
import androidx.cardview.widget.CardView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.bogleo.taskmanager.R

object Extensions {

    fun ImageView.setChecked(isChecked: Boolean) {
        val icon = when (isChecked) {
            true -> R.drawable.ic_tick
            false -> R.drawable.ic_circle_empty
        }
        setImageResource(icon)
    }

    fun CardView.setColor(color: Int) {
        backgroundTintList = ColorStateList.valueOf(color)
    }

    fun View.safeNavigateTo(action: NavDirections) {
        try {
            findNavController().navigate(action)
        } catch (e: Exception) {
            Log.e("SafeNavigate", "NavController error: ${e.localizedMessage}")
        }
    }

    fun DatePicker.getText(): String {
        return "${"%02d".format(dayOfMonth)}.${"%02d".format(month)}.${year}"
    }

    fun TimePicker.getText(): String {
        return "${"%02d".format(hour)}:${"%02d".format(minute)}"
    }
}