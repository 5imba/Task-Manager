package com.bogleo.taskmanager.common.adapters.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.bogleo.taskmanager.data.Task
import java.lang.IllegalArgumentException

class ShellAdapter(
    private val shells: List<TaskShell<*>>
) : ListAdapter<Task, BaseViewHolder<ViewBinding, Task>>(
    ShellDiffUtil(shells = shells)
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding, Task> {
        val inflater = LayoutInflater.from(parent.context)
        return shells.find { it.getLayoutId() == viewType }
            ?.getViewHolder(layoutInflater = inflater, parent = parent)
            ?: throw IllegalArgumentException("View type not found: $viewType")
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding, Task>, position: Int) {
        holder.onBind(currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        val task = currentList[position]
        return shells.find { it.isRelativeItem(task = task) }
            ?.getLayoutId()
            ?: throw IllegalArgumentException("View type not found: $task")
    }
}