package com.bogleo.taskmanager.screens.tasklist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.databinding.FragmentTaskListBinding
import com.bogleo.taskmanager.screens.TasksViewModel

class TaskListFragment : Fragment() {

    private val viewModel: TasksViewModel by activityViewModels()
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private var taskList: List<Task>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)

        // Observe task list data
        viewModel.readAllData.observe(viewLifecycleOwner) { taskList: List<Task> ->
            this.taskList = taskList
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}