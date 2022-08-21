package com.bogleo.taskmanager.screens.taskview

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.common.notification.NotificationHelper
import com.bogleo.taskmanager.data.change
import com.bogleo.taskmanager.databinding.FragmentTaskViewBinding
import com.bogleo.taskmanager.TasksViewModel

private const val TAG = "TaskView"

class TaskViewFragment : Fragment(), MenuProvider {

    private val mArgs by navArgs<TaskViewFragmentArgs>()
    private val mViewModel by activityViewModels<TasksViewModel>()
    private var _binding: FragmentTaskViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUi()
        configureMenu()
    }

    private fun setUi() {
        val task = mArgs.task
        binding.taskTitleTextTv.text = task.title
        binding.colorTagImageTv.setColorFilter(task.colorTag)
        binding.dateTextTv.text = task.date
        binding.timeTextTv.text = task.time
        binding.tagsTextTv.text = task.tags
        binding.setCompleteBtnTv.text = getText(
            if (task.isDone) R.string.set_unfulfilled
            else R.string.set_complete
        )

        binding.setCompleteBtnTv.setOnClickListener {
            val newTask = task.change(isDone = !task.isDone)
            mViewModel.updateTask(task = newTask) {
                NotificationHelper.setTaskNotification(
                    context = requireContext(),
                    task = newTask
                )
                navigateTo(
                    TaskViewFragmentDirections.actionTaskViewToTaskList()
                )
            }
        }
    }

    private fun navigateTo(action: NavDirections) {
        try {
            findNavController().navigate(action)
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.localizedMessage}}")
        }
    }

    private fun configureMenu() {
        val menuHost = requireActivity() as MenuHost
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_edit, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if(menuItem.itemId == R.id.editMenu){
            navigateTo(
                TaskViewFragmentDirections.actionTaskViewToTaskEditFragment(
                    task = mArgs.task
                )
            )
            return true
        }
        return false
    }
}