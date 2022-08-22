package com.bogleo.taskmanager.screens.taskaddnew

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.common.notification.NotificationHelper
import com.bogleo.taskmanager.common.TextUtils
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.data.change
import com.bogleo.taskmanager.databinding.FragmentTaskAddNewBinding
import com.bogleo.taskmanager.TasksViewModel
import com.bogleo.taskmanager.common.ColorTagPopup

private const val TAG = "TaskAddNew"

class TaskAddNewFragment : Fragment() {

    private val mViewModel: TasksViewModel by activityViewModels()
    private var _binding: FragmentTaskAddNewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskAddNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
    }

    private fun bindUi() {
        // Task title show warning on empty
        binding.taskTitleEditTextAn.addTextChangedListener { editable ->
            binding.taskTitleTextInputAn.error  =
                if (editable.isNullOrEmpty()) getString(R.string.empty_task_title_warning)
                else null
        }
        // Set color tag
        val colorTag = binding.colorTagImageAn.imageTintList?.defaultColor!!.toInt()
        binding.colorTagImageAn.tag = colorTag
        // Color tag Popup
        binding.colorTagImageAn.setOnClickListener {
            ColorTagPopup.show(
                viewAnchor = binding.colorTagImageAn,
                layoutInflater = layoutInflater,
                container = null
            )
        }
        // Date & Time pickers visibility
        binding.deadlineFrameContainerAn.setOnClickListener {
            setDeadlinePickersVisibility(isFocused = true)
            binding.deadlineEditTextAn.requestFocus()
        }
        binding.deadlineEditTextAn.setOnFocusChangeListener { _, isFocused ->
            setDeadlinePickersVisibility(isFocused = isFocused)
        }
        // Deadline text
        binding.datePickerAn.setOnDateChangedListener { _, _, _, _ ->
            setDeadlineText()
        }
        binding.timePickerAn.setOnTimeChangedListener { _, _, _ ->
            setDeadlineText()
        }
        // Tags hint visibility
        binding.tagsTextInputAn.errorIconDrawable = null
        binding.tagsEditTextAn.setOnFocusChangeListener { _, isFocused ->
            binding.tagsTextInputAn.error =
                if (isFocused) getString(R.string.tags_input_separator_hint)
                else null
        }
        // Add Task to DB
        binding.addTaskBtnAn.setOnClickListener { addTask() }
    }

    private fun setDeadlineText() {
        val dateTimeString = TextUtils.makeDateTimeText(
            datePicker = binding.datePickerAn,
            timePicker = binding.timePickerAn,
        )
        binding.deadlineEditTextAn.setText(dateTimeString)
    }

    private fun setDeadlinePickersVisibility(isFocused: Boolean) {
        if (isFocused) {
            closeKeyboard()
            setDeadlineText()
            binding.pickersLinearLayoutAn.visibility = View.VISIBLE
        } else {
            binding.pickersLinearLayoutAn.visibility = View.GONE
        }
    }

    private fun closeKeyboard() {
        val view: View? = requireActivity().currentFocus
        if (view != null) {
            val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun addTask() {
        if (!validateInput()) {
            binding.taskTitleTextInputAn.error = getString(R.string.empty_task_title_warning)
            return
        }

        val date = TextUtils.makeDateString(datePicker = binding.datePickerAn)
        val time = TextUtils.makeTimeString(timePicker = binding.timePickerAn)
        val timeMillis = TextUtils.getMillisFromDateTime(
            datePicker = binding.datePickerAn,
            timePicker = binding.timePickerAn
        )
        val task = Task(
            id = 0,
            title = binding.taskTitleEditTextAn.text.toString(),
            date = date,
            time = time,
            timeMillis = timeMillis,
            tags = binding.tagsEditTextAn.text.toString(),
            colorTag = binding.colorTagImageAn.tag as Int,
            isDone = false
        )
        mViewModel.addTask(task = task) { id: Long ->
            NotificationHelper.scheduleNotification(
                context = requireContext(),
                task = task.change(id = id)
            )
            navigateToTaskList()
        }
    }

    private fun validateInput(): Boolean {
        return !binding.taskTitleEditTextAn.text.isNullOrEmpty()
    }

    private fun navigateToTaskList() {
        try {
            val action = TaskAddNewFragmentDirections.actionTaskAddNewFragmentToTaskList()
            findNavController().navigate(action)
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.localizedMessage}}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}