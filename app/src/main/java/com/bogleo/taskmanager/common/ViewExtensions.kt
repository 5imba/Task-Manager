package com.bogleo.taskmanager.common

import android.content.res.ColorStateList
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.bogleo.taskmanager.R

object ViewExtensions {

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
}