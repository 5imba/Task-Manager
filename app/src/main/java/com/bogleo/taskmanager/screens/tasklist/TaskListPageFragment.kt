package com.bogleo.taskmanager.screens.tasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bogleo.taskmanager.common.DataListener
import com.bogleo.taskmanager.common.NotificationHelper
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.databinding.FragmentTaskListPageBinding
import com.bogleo.taskmanager.screens.TasksViewModel
import com.bogleo.taskmanager.screens.tasklist.adapters.TasksRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val TASK_LIST_PAGE_ARG = "taskListPageArg"

@AndroidEntryPoint
class TaskListPageFragment : Fragment() {

    @Inject
    lateinit var taskListAdapter: TasksRecyclerAdapter

    private val viewModel: TasksViewModel by activityViewModels()
    private var _binding: FragmentTaskListPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTaskListPageBinding.inflate(inflater, container, false)

        // RecyclerView
        binding.recyclerViewTlp.adapter = taskListAdapter
        binding.recyclerViewTlp.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.takeIf { it.containsKey(TASK_LIST_PAGE_ARG) }?.apply {

            taskListAdapter.dataListener = object : DataListener<Task> {
                override fun onDataChange(data: Task) {
                    viewModel.updateTask(task = data) {
                        NotificationHelper.setTaskNotification(
                            context = requireContext(),
                            task = data
                        )
                    }
                }
            }

            viewModel.readAllData.observe(viewLifecycleOwner) { taskList: List<Task> ->
                taskListAdapter.onDataChange(taskList.filter { task: Task ->
                    task.isDone == (getInt(TASK_LIST_PAGE_ARG) == 1)
                })
            }
        }
    }
}