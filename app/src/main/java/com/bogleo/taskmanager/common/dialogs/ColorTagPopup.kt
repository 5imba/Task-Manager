package com.bogleo.taskmanager.common.dialogs

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import com.bogleo.taskmanager.R

object ColorTagPopup {

    fun show(
        viewAnchor: ImageView,
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ) {
        val popupView = layoutInflater.inflate(R.layout.layout_color_tag, container)
        val popup = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        popup.isOutsideTouchable = true
        popup.isFocusable = true
        popup.elevation = 10.0f

        val iv1 = popup.contentView.findViewById<ImageView>(R.id.imageColorTag1)
        iv1.setOnClickListener{ setColorTag(iv1, viewAnchor, popup) }

        val iv2 = popup.contentView.findViewById<ImageView>(R.id.imageColorTag2)
        iv2.setOnClickListener{ setColorTag(iv2, viewAnchor, popup) }

        val iv3 = popup.contentView.findViewById<ImageView>(R.id.imageColorTag3)
        iv3.setOnClickListener{ setColorTag(iv3, viewAnchor, popup) }

        val iv4 = popup.contentView.findViewById<ImageView>(R.id.imageColorTag4)
        iv4.setOnClickListener{ setColorTag(iv4, viewAnchor, popup) }

        val iv5 = popup.contentView.findViewById<ImageView>(R.id.imageColorTag5)
        iv5.setOnClickListener{ setColorTag(iv5, viewAnchor, popup) }

        val iv6 = popup.contentView.findViewById<ImageView>(R.id.imageColorTag6)
        iv6.setOnClickListener{ setColorTag(iv6, viewAnchor, popup) }

        val iv7 = popup.contentView.findViewById<ImageView>(R.id.imageColorTag7)
        iv7.setOnClickListener{ setColorTag(iv7, viewAnchor, popup) }

        val iv8 = popup.contentView.findViewById<ImageView>(R.id.imageColorTag8)
        iv8.setOnClickListener{ setColorTag(iv8, viewAnchor, popup) }

        popup.showAsDropDown(viewAnchor)
    }

    private fun setColorTag(fromView: ImageView, toView: ImageView, popup: PopupWindow) {
        val colorTag = fromView.imageTintList?.defaultColor!!.toInt()
        toView.setColorFilter(colorTag)
        toView.tag = colorTag
        popup.dismiss()
    }
}