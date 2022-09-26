package com.bogleo.taskmanager.common.adapters.recycler

import androidx.recyclerview.widget.DiffUtil
import com.bogleo.taskmanager.data.Task

class ShellDiffUtil(
    private val shells: List<TaskShell<*>>
) : DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        if (oldItem::class != newItem::class) return false

        return getItemCallback(oldItem).areItemsTheSame(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        if (oldItem::class != newItem::class) return false

        return getItemCallback(oldItem).areContentsTheSame(oldItem, newItem)
    }

    private fun getItemCallback(
        task: Task
    ): DiffUtil.ItemCallback<Task> = shells.find { it.isRelativeItem(task) }
        ?.getDiffUtil()
        ?: throw IllegalStateException("DiffUtil not found for $task")
}