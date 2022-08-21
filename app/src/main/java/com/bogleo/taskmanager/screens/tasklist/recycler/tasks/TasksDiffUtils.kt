package com.bogleo.taskmanager.screens.tasklist.recycler.tasks

import androidx.recyclerview.widget.DiffUtil
import com.bogleo.taskmanager.data.Task

class TasksDiffUtils(
    private val oldList: List<Task>,
    private val newList: List<Task>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}