package com.bogleo.taskmanager.screens.edit

import android.app.Activity
import android.app.AlertDialog
import android.content.res.ColorStateList
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.common.notification.NotificationHelper
import com.bogleo.taskmanager.common.TextUtils
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.databinding.FragmentTaskEditBinding
import com.bogleo.taskmanager.screens.MainViewModel
import com.bogleo.taskmanager.common.dialogs.ColorTagPopup

private val TAG = TaskEditFragment::class.qualifiedName

class TaskEditFragment : Fragment(), MenuProvider {

    private var _binding: FragmentTaskEditBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: TaskEditViewModel by viewModels()
    private val mArgs by navArgs<TaskEditFragmentArgs>()

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

        with(binding) {
            teEdTxtTitle.setText(task.title)
            teTxtInTitle.setEndIconTintList(ColorStateList.valueOf(task.colorTag))
            teTxtInTitle.tag = task.colorTag
            teEdTxtDeadline.setText(deadline)
            teEdTxtTags.setText(TextUtils.makeTagString(task.tags))
            teSwitchCompletion.isChecked = task.isDone
        }
    }

    private fun bindUiActions() {
        with(binding) {
            // Task title show warning on empty
            teEdTxtTitle.addTextChangedListener { editable ->
                binding.teTxtInTitle.error  =
                    if (editable.isNullOrEmpty()) getString(R.string.empty_task_title_warning)
                    else null
            }
            // Color tag Popup
            teTxtInTitle.setEndIconOnClickListener {
                ColorTagPopup.show(
                    viewAnchor = teTxtInTitle,
                    layoutInflater = layoutInflater,
                    container = null
                )
            }
            // Date & Time pickers visibility
            teFrameDeadline.setOnClickListener {
                setDeadlinePickersVisibility(isFocused = true)
                binding.teEdTxtDeadline.requestFocus()
            }
            teEdTxtDeadline.setOnFocusChangeListener { _, isFocused ->
                setDeadlinePickersVisibility(isFocused = isFocused)
            }
            // Deadline text
            tePickerDate.setOnDateChangedListener { _, _, _, _ ->
                setDeadlineText()
            }
            tePickerTime.setOnTimeChangedListener { _, _, _ ->
                setDeadlineText()
            }
            // Tags hint visibility
            teTxtInTags.errorIconDrawable = null
            teEdTxtTags.setOnFocusChangeListener { _, isFocused ->
                binding.teTxtInTags.error =
                    if (isFocused) getString(R.string.tags_input_separator_hint)
                    else null
            }
            // Save Task to DB
            teBtnSaveTask.setOnClickListener { saveChanges() }
        }
    }

    private fun setDeadlineText() {
        val dateTimeString = TextUtils.makeDateTimeText(
            datePicker = binding.tePickerDate,
            timePicker = binding.tePickerTime,
        )
        binding.teEdTxtDeadline.setText(dateTimeString)
    }

    private fun setDeadlinePickersVisibility(isFocused: Boolean) {
        if (isFocused) {
            closeKeyboard()
            setDeadlineText()
            binding.teLinearPickers.visibility = View.VISIBLE
        } else {
            binding.teLinearPickers.visibility = View.GONE
        }
    }

    private fun closeKeyboard() {
        val view: View? = requireActivity().currentFocus
        if (view != null) {
            val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun saveChanges() {
        if (!viewModel.validateInput(binding)) {
            binding.teTxtInTitle.error = getString(R.string.empty_task_title_warning)
            return
        }

        val task = viewModel.buildTask(mArgs.task, binding)
        mainViewModel.updateTask(task) {
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
                mainViewModel.deleteTask(task) {
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
            Log.e(TAG, "NavController error: ${e.localizedMessage}}")
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}