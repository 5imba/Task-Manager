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
        with(binding) {
            val timeMillis = TextUtils.getMillisFromDateTime(
                datePicker = tePickerDate,
                timePicker = tePickerTime
            )
            val tags = TextUtils.makeTagList(
                tagsStr = teEdTxtTags.text.toString()
            )

            return Task(
                id = currentTask.id,
                title = teEdTxtTitle.text.toString(),
                date = tePickerDate.getText(),
                time = tePickerTime.getText(),
                timeMillis = timeMillis,
                tags = tags,
                colorTag = teTxtInTitle.tag as Int,
                isDone = teSwitchCompletion.isChecked
            )
        }
    }

}