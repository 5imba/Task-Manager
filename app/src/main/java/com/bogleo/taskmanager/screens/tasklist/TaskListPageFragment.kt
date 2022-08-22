package com.bogleo.taskmanager.screens.tasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogleo.taskmanager.common.DataListener
import com.bogleo.taskmanager.common.notification.NotificationHelper
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.databinding.FragmentTaskListPageBinding
import com.bogleo.taskmanager.TasksViewModel
import com.bogleo.taskmanager.screens.tasklist.recycler.tasks.TasksDiffUtils
import com.bogleo.taskmanager.screens.tasklist.recycler.tasks.TasksRecyclerAdapter

const val TASK_LIST_PAGE_ARG = "taskListPageArg"

class TaskListPageFragment : Fragment() {

    private val mViewModel: TasksViewModel by activityViewModels()
    private var _binding: FragmentTaskListPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskListAdapter: TasksRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTaskListPageBinding.inflate(inflater, container, false)

        // RecyclerView configs
        taskListAdapter = TasksRecyclerAdapter()
        binding.recyclerViewTlp.adapter = taskListAdapter
        binding.recyclerViewTlp.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(TASK_LIST_PAGE_ARG) }?.apply {
            // Callback from adapter
            taskListAdapter.setOnDataChangeListener(object : DataListener<Task> {
                override fun onDataChange(data: Task) {
                    mViewModel.updateTask(task = data) {
                        NotificationHelper.setTaskNotification(
                            context = requireContext(),
                            task = data
                        )
                    }
                }
            })
            // Observe Task DB data
            mViewModel.readAllData.observe(viewLifecycleOwner) { taskList: List<Task> ->
                setDataToRecycler(
                    taskList = taskList,
                    pageNum = getInt(TASK_LIST_PAGE_ARG)
                )
            }
        }
    }

    private fun setDataToRecycler(taskList: List<Task>, pageNum: Int) {
        // Sort by isDone filter
        val newTaskList = taskList.filter { task: Task ->
            task.isDone == (pageNum == 1)
        }
        // Calculate changes
        val diffUtils = TasksDiffUtils(
            oldList = taskListAdapter.getData(),
            newList = newTaskList
        )
        val diffResult = DiffUtil.calculateDiff(diffUtils)
        // Apply data
        taskListAdapter.setData(data = newTaskList)
        diffResult.dispatchUpdatesTo(taskListAdapter)
    }
}