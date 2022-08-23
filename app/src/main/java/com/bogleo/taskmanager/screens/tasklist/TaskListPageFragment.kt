package com.bogleo.taskmanager.screens.tasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogleo.taskmanager.common.notification.NotificationHelper
import com.bogleo.taskmanager.model.Task
import com.bogleo.taskmanager.databinding.FragmentTaskListPageBinding
import com.bogleo.taskmanager.TasksViewModel
import com.bogleo.taskmanager.adapters.recycler.ShellAdapter
import com.bogleo.taskmanager.adapters.recycler.shells.TaskNoTagsShell
import com.bogleo.taskmanager.adapters.recycler.shells.TaskWithTagsShell

const val TASK_LIST_PAGE_ARG = "taskListPageArg"

class TaskListPageFragment : Fragment() {

    private var _binding: FragmentTaskListPageBinding? = null
    private val binding get() = _binding!!

    private val mViewModel: TasksViewModel by activityViewModels()
    private lateinit var recyclerAdapter: ShellAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTaskListPageBinding.inflate(inflater, container, false)

        // RecyclerView configs
        recyclerAdapter = ShellAdapter(getShells())
        with(binding.recyclerViewTlp) {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(TASK_LIST_PAGE_ARG) }?.apply {
            // Observe Task DB data
            mViewModel.readAllData.observe(viewLifecycleOwner) { taskList: List<Task> ->
                setDataToRecycler(
                    taskList = taskList,
                    pageNum = getInt(TASK_LIST_PAGE_ARG)
                )
            }
        }
    }

    private fun getShells() = listOf(
        TaskWithTagsShell(::onTaskStateChange),
        TaskNoTagsShell(::onTaskStateChange)
    )

    private fun setDataToRecycler(taskList: List<Task>, pageNum: Int) {
        // Sort by isDone filter
        val newTaskList = taskList.filter { task: Task ->
            task.isDone == (pageNum == 1)
        }
        // Apply data
        recyclerAdapter.submitList(newTaskList)
    }

    private fun onTaskStateChange(task: Task) {
        mViewModel.updateTask(task = task) {
            NotificationHelper.setTaskNotification(
                context = requireContext(),
                task = task
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}