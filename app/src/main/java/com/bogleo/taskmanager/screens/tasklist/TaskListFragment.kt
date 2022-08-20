package com.bogleo.taskmanager.screens.tasklist

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.databinding.FragmentTaskListBinding
import com.bogleo.taskmanager.screens.tasklist.adapters.TasksPagerAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "TaskList"

@AndroidEntryPoint
class TaskListFragment: Fragment(), MenuProvider {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        // Set ViewPager adapter TODO does it need DI?
        binding.viewPagerTl.adapter = TasksPagerAdapter(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener {
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

    private fun configureMenu() {
        val menuHost = requireActivity() as MenuHost
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_search, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if(menuItem.itemId == R.id.searchMenu) {
            return true
        }
        return false
    }
}