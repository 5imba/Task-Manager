package com.bogleo.taskmanager.screens.addnew

import android.app.Activity
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.common.notification.NotificationHelper
import com.bogleo.taskmanager.common.TextUtils
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.databinding.FragmentTaskAddNewBinding
import com.bogleo.taskmanager.screens.MainViewModel
import com.bogleo.taskmanager.common.getText
import com.bogleo.taskmanager.common.dialogs.ColorTagPopup

private val TAG = TaskAddNewFragment::class.qualifiedName

class TaskAddNewFragment : Fragment() {

    private var _binding: FragmentTaskAddNewBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: TaskAddNewViewModel by viewModels()

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
        with(binding) {
            // Task title show warning on empty
            tanEdTxtTitle.addTextChangedListener { editable ->
                tanTxtInTitle.error  =
                    if (editable.isNullOrEmpty()) getString(R.string.empty_task_title_warning)
                    else null
            }
            // Set color tag
            val colorTag = requireContext().getColor(R.color.green)
            tanTxtInTitle.setEndIconTintList(ColorStateList.valueOf(colorTag))
            tanTxtInTitle.tag = colorTag
            // Color tag Popup
            tanTxtInTitle.setEndIconOnClickListener {
                ColorTagPopup.show(
                    viewAnchor = tanTxtInTitle,
                    layoutInflater = layoutInflater,
                    container = null
                )
            }
            // Date & Time pickers visibility
            tanFrameDeadlineContainer.setOnClickListener {
                setDeadlinePickersVisibility(isFocused = true)
                tanEdTxtDeadline.requestFocus()
            }
            tanEdTxtDeadline.setOnFocusChangeListener { _, isFocused ->
                setDeadlinePickersVisibility(isFocused = isFocused)
            }
            // Deadline text
            tanPickerDate.setOnDateChangedListener { _, _, _, _ ->
                setDeadlineText()
            }
            tanPickerTime.setOnTimeChangedListener { _, _, _ ->
                setDeadlineText()
            }
            // Tags hint visibility
            tanTxtInTags.errorIconDrawable = null
            tanEdTxtTags.setOnFocusChangeListener { _, isFocused ->
                tanTxtInTags.error =
                    if (isFocused) getString(R.string.tags_input_separator_hint)
                    else null
            }
            // Add Task to DB
            tanBtnAddTask.setOnClickListener { addTask() }
        }
    }

    private fun setDeadlineText() {
        val dateTimeString = TextUtils.makeDateTimeText(
            datePicker = binding.tanPickerDate,
            timePicker = binding.tanPickerTime,
        )
        binding.tanEdTxtDeadline.setText(dateTimeString)
    }

    private fun setDeadlinePickersVisibility(isFocused: Boolean) {
        if (isFocused) {
            closeKeyboard()
            setDeadlineText()
            binding.tanLinearPickers.visibility = View.VISIBLE
        } else {
            binding.tanLinearPickers.visibility = View.GONE
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
        if (!viewModel.validateInput(binding)) {
            binding.tanTxtInTitle.error = getString(R.string.empty_task_title_warning)
            return
        }

        val task = viewModel.buildTask(binding)
        mainViewModel.addTask(task = task) { id: Long ->
            NotificationHelper.scheduleNotification(
                context = requireContext(),
                task = task.copy(id = id)
            )
            navigateToTaskList()
        }
    }

    private fun navigateToTaskList() {
        try {
            val action = TaskAddNewFragmentDirections.actionTaskAddNewFragmentToTaskList()
            findNavController().navigate(action)
        } catch (e: Exception) {
            Log.e(TAG, "NavController error: ${e.localizedMessage}}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}