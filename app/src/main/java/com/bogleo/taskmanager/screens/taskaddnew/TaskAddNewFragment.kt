package com.bogleo.taskmanager.screens.taskaddnew

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bogleo.taskmanager.common.notification.NotificationHelper
import com.bogleo.taskmanager.common.Utils
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.data.change
import com.bogleo.taskmanager.databinding.FragmentTaskAddNewBinding
import com.bogleo.taskmanager.TasksViewModel

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
        binding.deadlineFrameContainerAn.setOnClickListener {
            setDeadlinePickersVisibility(isFocused = true)
            binding.deadlineEditTextAn.requestFocus()
        }

        binding.addTaskBtnAn.setOnClickListener { addTask() }

        binding.deadlineEditTextAn.setOnFocusChangeListener { _, isFocused ->
            setDeadlinePickersVisibility(isFocused = isFocused)
        }

        binding.datePickerAn.setOnDateChangedListener { _, _, _, _ ->
            setDeadlineText()
        }
        binding.timePickerAn.setOnTimeChangedListener { _, _, _ ->
            setDeadlineText()
        }

        binding.colorTagImageAn.setOnClickListener {
            Utils.makeColorTagPopup(
                viewAnchor = binding.colorTagImageAn,
                layoutInflater = layoutInflater,
                container = null
            )
        }
    }

    private fun setDeadlineText() {
        val dateTimeString = Utils.makeDateTimeText(
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
        val date = Utils.makeDateString(datePicker = binding.datePickerAn)
        val time = Utils.makeTimeString(timePicker = binding.timePickerAn)
        val timeMillis = Utils.getMillisFromDateTime(
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