package com.bogleo.taskmanager.screens.tasklist

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.databinding.FragmentTaskListBinding
import com.bogleo.taskmanager.TasksViewModel
import com.bogleo.taskmanager.common.DataListener
import com.bogleo.taskmanager.common.notification.NotificationHelper
import com.bogleo.taskmanager.screens.tasklist.recycler.tasks.TasksDiffUtils
import com.bogleo.taskmanager.screens.tasklist.pager.TasksPagerAdapter
import com.bogleo.taskmanager.screens.tasklist.recycler.tasks.TasksRecyclerAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "TaskList"

@AndroidEntryPoint
class TaskListFragment: Fragment(), MenuProvider {

    @Inject
    lateinit var taskListAdapter: TasksRecyclerAdapter
    private val mViewModel: TasksViewModel by activityViewModels()
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        // Set ViewPager adapter
        binding.viewPagerTl.adapter = TasksPagerAdapter(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUiListeners()
        configureSearchRecycler()
        configureMenu()
    }

    override fun onStop() {
        super.onStop()
        binding.viewPagerTl.unregisterOnPageChangeCallback(onPageChangeCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.completionTabLayoutTl.getTabAt(position)?.select()
        }
    }

    private fun bindUiListeners() {
        binding.addTaskButtonTl.setOnClickListener {
            try {
                val action = TaskListFragmentDirections.actionTaskListToTaskAddNewFragment()
                findNavController().navigate(action)
            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.localizedMessage}}")
            }
        }

        binding.completionTabLayoutTl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPagerTl.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) { }
            override fun onTabReselected(tab: TabLayout.Tab) { }
        })

        binding.viewPagerTl.registerOnPageChangeCallback(onPageChangeCallback)
    }

    private fun configureSearchRecycler() {
        binding.searchRecyclerTl.adapter = taskListAdapter
        binding.searchRecyclerTl.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )
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
    }

    private fun configureMenu() {
        val menuHost = requireActivity() as MenuHost
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_search, menu)

        val search = menu.findItem(R.id.searchMenu)
        val searchView = search.actionView as SearchView

        searchView.apply {
            isSubmitButtonEnabled = false
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return searchTask(query = query)
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    return searchTask(query = newText)
                }
            })
        }
        search.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(menuItem: MenuItem?): Boolean {
                binding.searchFrameTl.visibility = View.VISIBLE
                return true
            }
            override fun onMenuItemActionCollapse(menuItem: MenuItem?): Boolean {
                binding.searchFrameTl.visibility = View.GONE
                return true
            }
        })
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if(menuItem.itemId == R.id.searchMenu) {
            return true
        }
        return false
    }

    private fun searchTask(query: String?): Boolean {
        if (query != null) {
            val searchQuery = "%$query%"
            mViewModel.searchTask(searchQuery).observe(viewLifecycleOwner) { tasks: List<Task> ->
                // Calculate changes
                val diffUtils = TasksDiffUtils(
                    oldList = taskListAdapter.getData(),
                    newList = tasks
                )
                val diffResult = DiffUtil.calculateDiff(diffUtils)
                // Apply data
                taskListAdapter.setData(data = tasks)
                diffResult.dispatchUpdatesTo(taskListAdapter)
            }
            return true
        }
        return false
    }
}