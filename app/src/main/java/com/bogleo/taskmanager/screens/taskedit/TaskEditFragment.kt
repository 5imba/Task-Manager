package com.bogleo.taskmanager.screens.taskedit

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.common.notification.NotificationHelper
import com.bogleo.taskmanager.common.TextUtils
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.databinding.FragmentTaskEditBinding
import com.bogleo.taskmanager.TasksViewModel
import com.bogleo.taskmanager.ui.ColorTagPopup

private const val TAG = "TaskEdit"

class TaskEditFragment : Fragment(), MenuProvider {

    private val mArgs by navArgs<TaskEditFragmentArgs>()
    private val mViewModel: TasksViewModel by activityViewModels()
    private var _binding: FragmentTaskEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUiData()
        bindUiActions()
        configureMenu()
    }

    private fun setUiData() {
        val task = mArgs.task
        val deadline = "${task.date}, ${task.time}"

        binding.taskTitleEditTextTe.setText(task.title)
        binding.colorTagImageTe.setColorFilter(task.colorTag)
        binding.colorTagImageTe.tag = task.colorTag
        binding.deadlineEditTextTe.setText(deadline)
        binding.tagsEditTextTe.setText(task.tags)
        binding.setCompletionSwitchTe.isChecked = task.isDone
    }

    private fun bindUiActions() {
        // Task title show warning on empty
        binding.taskTitleEditTextTe.addTextChangedListener { editable ->
            binding.taskTitleTextInputTe.error  =
                if (editable.isNullOrEmpty()) getString(R.string.empty_task_title_warning)
                else null
        }
        // Color tag Popup
        binding.colorTagImageTe.setOnClickListener {
            ColorTagPopup.show(
                viewAnchor = binding.colorTagImageTe,
                layoutInflater = layoutInflater,
                container = null
            )
        }
        // Date & Time pickers visibility
        binding.deadlineFrameContainerTe.setOnClickListener {
            setDeadlinePickersVisibility(isFocused = true)
            binding.deadlineEditTextTe.requestFocus()
        }
        binding.deadlineEditTextTe.setOnFocusChangeListener { _, isFocused ->
            setDeadlinePickersVisibility(isFocused = isFocused)
        }
        // Deadline text
        binding.datePickerTe.setOnDateChangedListener { _, _, _, _ ->
            setDeadlineText()
        }
        binding.timePickerTe.setOnTimeChangedListener { _, _, _ ->
            setDeadlineText()
        }
        // Tags hint visibility
        binding.tagsTextInputTe.errorIconDrawable = null
        binding.tagsEditTextTe.setOnFocusChangeListener { _, isFocused ->
            binding.tagsTextInputTe.error =
                if (isFocused) getString(R.string.tags_input_separator_hint)
                else null
        }
        // Save Task to DB
        binding.saveTaskBtnTe.setOnClickListener { saveChanges() }
    }

    private fun setDeadlineText() {
        val dateTimeString = TextUtils.makeDateTimeText(
            datePicker = binding.datePickerTe,
            timePicker = binding.timePickerTe,
        )
        binding.deadlineEditTextTe.setText(dateTimeString)
    }

    private fun setDeadlinePickersVisibility(isFocused: Boolean) {
        if (isFocused) {
            closeKeyboard()
            setDeadlineText()
            binding.pickersLinearLayoutTe.visibility = View.VISIBLE
        } else {
            binding.pickersLinearLayoutTe.visibility = View.GONE
        }
    }

    private fun closeKeyboard() {
        val view: View? = requireActivity().currentFocus
        if (view != null) {
            val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun validateInput(): Boolean {
        return !binding.taskTitleEditTextTe.text.isNullOrEmpty()
    }

    private fun saveChanges() {
        if (!validateInput()) {
            binding.tagsTextInputTe.error = getString(R.string.tags_input_separator_hint)
            return
        }

        val date = TextUtils.makeDateString(datePicker = binding.datePickerTe)
        val time = TextUtils.makeTimeString(timePicker = binding.timePickerTe)
        val timeMillis = TextUtils.getMillisFromDateTime(
            datePicker = binding.datePickerTe,
            timePicker = binding.timePickerTe
        )
        val task = Task(
            id = mArgs.task.id,
            title = binding.taskTitleEditTextTe.text.toString(),
            date = date,
            time = time,
            timeMillis = timeMillis,
            tags = binding.tagsEditTextTe.text.toString(),
            colorTag = binding.colorTagImageTe.tag as Int,
            isDone = binding.setCompletionSwitchTe.isChecked
        )
        mViewModel.updateTask(task) {
            NotificationHelper.setTaskNotification(
                context = requireContext(),
                task =  task
            )
            navigateToTaskList()
        }
    }

    private fun configureMenu() {
        val menuHost = requireActivity() as MenuHost
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun deleteTask(task: Task) {
        AlertDialog.Builder(requireContext()).apply {
            setPositiveButton(getText(R.string.yes)) { _, _ ->
                mViewModel.deleteTask(task) {
                    NotificationHelper.removeNotification(
                        context = requireContext(),
                        task = task
                    )
                    Toast.makeText(
                        requireContext(),
                        getText(R.string.task_deleted),
                        Toast.LENGTH_LONG).show()
                    navigateToTaskList()
                }
            }
            setNegativeButton(R.string.no) { _, _ -> }
            setTitle("${getText(R.string.delete_task)} \"${task.title}\"?")
            setMessage("${getText(R.string.task_delete_warning)} \"${task.title}\"?")
            create()
            show()
        }
    }

    private fun navigateToTaskList() {
        try {
            val action = TaskEditFragmentDirections.actionTaskEditFragmentToTaskList()
            findNavController().navigate(action)
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.localizedMessage}}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_delete, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if(menuItem.itemId == R.id.deleteMenu){
            deleteTask(task = mArgs.task)
            return true
        }
        return false
    }
}