package com.bogleo.taskmanager.screens.tasklist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bogleo.taskmanager.databinding.FragmentTaskListBinding
import com.bogleo.taskmanager.screens.tasklist.adapters.TasksPagerAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "TaskList"

@AndroidEntryPoint
class TaskListFragment: Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        // Set ViewPager adapter TODO does it need DI
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
}