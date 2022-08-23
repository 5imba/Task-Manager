package com.bogleo.taskmanager.adapters.pager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bogleo.taskmanager.screens.tasklist.TASK_LIST_PAGE_ARG
import com.bogleo.taskmanager.screens.tasklist.TaskListPageFragment
import javax.inject.Inject

class TasksPagerAdapter @Inject constructor(fragment: Fragment)
    : FragmentStateAdapter(fragment) {

    // Pages count
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = TaskListPageFragment()

        // Put page index
        fragment.arguments = Bundle().apply {
            putInt(TASK_LIST_PAGE_ARG, position)
        }

        return fragment
    }
}