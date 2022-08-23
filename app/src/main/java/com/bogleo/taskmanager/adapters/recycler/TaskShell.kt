package com.bogleo.taskmanager.adapters.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.bogleo.taskmanager.model.Task

interface TaskShell<V: ViewBinding> {

    fun isRelativeItem(task: Task): Boolean

    @LayoutRes
    fun getLayoutId(): Int

    fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<V, Task>

    fun getDiffUtil(): DiffUtil.ItemCallback<Task>
}