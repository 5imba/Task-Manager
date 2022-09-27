package com.bogleo.taskmanager.screens.addnew

import androidx.lifecycle.ViewModel
import com.bogleo.taskmanager.common.TextUtils
import com.bogleo.taskmanager.common.getText
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.databinding.FragmentTaskAddNewBinding

class TaskAddNewViewModel : ViewModel() {

    fun validateInput(binding: FragmentTaskAddNewBinding): Boolean {
        return !binding.tanEdTxtTitle.text.isNullOrEmpty()
    }

    fun buildTask(binding: FragmentTaskAddNewBinding): Task {
        with(binding) {
            val timeMillis = TextUtils.getMillisFromDateTime(
                datePicker = tanPickerDate,
                timePicker = tanPickerTime
            )
            val tags = TextUtils.makeTagList(
                tagsStr = tanEdTxtTags.text.toString()
            )

            return Task(
                id = 0,
                title = tanEdTxtTitle.text.toString(),
                date = tanPickerDate.getText(),
                time = tanPickerTime.getText(),
                timeMillis = timeMillis,
                tags = tags,
                colorTag = tanTxtInTitle.tag as Int,
                isDone = false
            )
        }
    }
}