package com.bogleo.taskmanager.common.dialogs

import android.content.res.ColorStateList
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.core.view.doOnPreDraw
import com.bogleo.taskmanager.R
import com.google.android.material.textfield.TextInputLayout


object ColorTagPopup {

    fun show(
        viewAnchor: TextInputLayout,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
    ) {
        val popupView = layoutInflater.inflate(R.layout.layout_color_tag, container)
        val popup = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            isOutsideTouchable = true
            isFocusable = true
            elevation = 10.0f

            val iv1 = contentView.findViewById<ImageView>(R.id.imageColorTag1)
            iv1.setOnClickListener { setColorTag(iv1, viewAnchor, this) }

            val iv2 = contentView.findViewById<ImageView>(R.id.imageColorTag2)
            iv2.setOnClickListener { setColorTag(iv2, viewAnchor, this) }

            val iv3 = contentView.findViewById<ImageView>(R.id.imageColorTag3)
            iv3.setOnClickListener { setColorTag(iv3, viewAnchor, this) }

            val iv4 = contentView.findViewById<ImageView>(R.id.imageColorTag4)
            iv4.setOnClickListener { setColorTag(iv4, viewAnchor, this) }

            val iv5 = contentView.findViewById<ImageView>(R.id.imageColorTag5)
            iv5.setOnClickListener { setColorTag(iv5, viewAnchor, this) }

            val iv6 = contentView.findViewById<ImageView>(R.id.imageColorTag6)
            iv6.setOnClickListener { setColorTag(iv6, viewAnchor, this) }

            val iv7 = contentView.findViewById<ImageView>(R.id.imageColorTag7)
            iv7.setOnClickListener { setColorTag(iv7, viewAnchor, this) }

            val iv8 = contentView.findViewById<ImageView>(R.id.imageColorTag8)
            iv8.setOnClickListener { setColorTag(iv8, viewAnchor, this) }
        }

        // Measure X offset
        popupView.measure(
            makeDropDownMeasureSpec(popup.width),
            makeDropDownMeasureSpec(popup.height)
        )
        val offsetX = popupView.measuredWidth
        // Show popup
        popup.showAsDropDown(viewAnchor, -offsetX, 0, Gravity.END)
    }

    private fun setColorTag(fromView: ImageView, toView: TextInputLayout, popup: PopupWindow) {
        val colorTag = fromView.imageTintList?.defaultColor!!.toInt()
        toView.setEndIconTintList(ColorStateList.valueOf(colorTag))
        toView.tag = colorTag
        popup.dismiss()
    }

    private fun makeDropDownMeasureSpec(measureSpec: Int): Int {
        val mode: Int = if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            View.MeasureSpec.UNSPECIFIED
        } else {
            View.MeasureSpec.EXACTLY
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode)
    }
}