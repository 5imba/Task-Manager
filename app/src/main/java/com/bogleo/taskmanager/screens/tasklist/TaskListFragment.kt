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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.model.Task
import com.bogleo.taskmanager.databinding.FragmentTaskListBinding
import com.bogleo.taskmanager.TasksViewModel
import com.bogleo.taskmanager.common.notification.NotificationHelper
import com.bogleo.taskmanager.adapters.pager.TasksPagerAdapter
import com.bogleo.taskmanager.adapters.recycler.ShellAdapter
import com.bogleo.taskmanager.adapters.recycler.shells.TaskNoTagsShell
import com.bogleo.taskmanager.adapters.recycler.shells.TaskWithTagsShell
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

private val TAG = TaskListFragment::class.qualifiedName

@AndroidEntryPoint
class TaskListFragment: Fragment(), MenuProvider {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private val mViewModel: TasksViewModel by activityViewModels()
    private val pagerAdapter by lazy { TasksPagerAdapter(this) }
    private lateinit var recyclerAdapter: ShellAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)

        // RecyclerView configs
        recyclerAdapter = ShellAdapter(getShells())
        with(binding.searchRecyclerTl) {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
            )
        }
        // Pager configs
        binding.viewPagerTl.adapter = pagerAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUiListeners()
        configureMenu()
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
                Log.e(TAG, "NavController error: ${e.localizedMessage}}")
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

    private fun getShells() = listOf(
        TaskWithTagsShell(::onTaskStateChange),
        TaskNoTagsShell(::onTaskStateChange)
    )

    private fun onTaskStateChange(task: Task) {
        mViewModel.updateTask(task = task) {
            NotificationHelper.setTaskNotification(
                context = requireContext(),
                task = task
            )
        }
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
                recyclerAdapter.submitList(tasks)
            }
            return true
        }
        return false
    }

    override fun onStop() {
        super.onStop()
        binding.viewPagerTl.unregisterOnPageChangeCallback(onPageChangeCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}