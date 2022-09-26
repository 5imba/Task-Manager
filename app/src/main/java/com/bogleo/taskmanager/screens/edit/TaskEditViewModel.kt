package com.bogleo.taskmanager.screens.edit

import androidx.lifecycle.ViewModel
import com.bogleo.taskmanager.common.TextUtils
import com.bogleo.taskmanager.common.getText
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.databinding.FragmentTaskEditBinding
import com.google.android.material.textfield.TextInputEditText

class TaskEditViewModel : ViewModel() {

    fun validateInput(binding: FragmentTaskEditBinding): Boolean {
        return !binding.teEdTxtTitle.text.isNullOrEmpty()
    }

    fun buildTask(currentTask: Task, binding: FragmentTaskEditBinding): Task {
        val timeMillis = TextUtils.getMillisFromDateTime(
            datePicker = binding.tePickerDate,
            timePicker = binding.tePickerTime
        )
        val tags = TextUtils.makeTagList(
            tagsStr = binding.teEdTxtTags.text.toString()
        )

        return Task(
            id = currentTask.id,
            title = binding.teEdTxtTitle.text.toString(),
            date = binding.tePickerDate.getText(),
            time = binding.tePickerTime.getText(),
            timeMillis = timeMillis,
            tags = tags,
            colorTag = binding.teImgColorTag.tag as Int,
            isDone = binding.teSwitchCompletion.isChecked
        )
    }

}