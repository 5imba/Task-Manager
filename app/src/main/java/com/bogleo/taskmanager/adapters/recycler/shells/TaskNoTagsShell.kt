package com.bogleo.taskmanager.adapters.recycler.shells

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.adapters.recycler.TaskShell
import com.bogleo.taskmanager.adapters.recycler.BaseViewHolder
import com.bogleo.taskmanager.common.Extensions.safeNavigateTo
import com.bogleo.taskmanager.common.Extensions.setChecked
import com.bogleo.taskmanager.common.Extensions.setColor
import com.bogleo.taskmanager.model.Task
import com.bogleo.taskmanager.databinding.ItemTaskNoTagsBinding
import com.bogleo.taskmanager.screens.tasklist.TaskListFragmentDirections

class TaskNoTagsShell(
    private val onStateChange: (Task) -> Unit
) : TaskShell<ItemTaskNoTagsBinding> {

    override fun isRelativeItem(task: Task) = task.tags.isEmpty()

    override fun getLayoutId() = R.layout.item_task_no_tags

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemTaskNoTagsBinding, Task> {
        val binding = ItemTaskNoTagsBinding.inflate(layoutInflater, parent, false)
        return TaskNoTagsViewHolder(binding = binding, onStateChange = onStateChange)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldTask: Task, newTask: Task) = oldTask.id == newTask.id
        override fun areContentsTheSame(oldTask: Task, newTask: Task) = oldTask == newTask
    }
}

class TaskNoTagsViewHolder(
    binding: ItemTaskNoTagsBinding,
    val onStateChange: (Task) -> Unit
) : BaseViewHolder<ItemTaskNoTagsBinding, Task>(binding = binding) {

    init {
        // Switch task state
        binding.tntImageState.setOnClickListener {
            if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
            onStateChange(item.copy(isDone = !item.isDone))
        }
        // Navigate to view task
        binding.tntCardContainer.setOnClickListener {
            val action = TaskListFragmentDirections
                .actionTaskListToTaskView(task = item)
            itemView.safeNavigateTo(action = action)
        }
        // Navigate to edit task
        binding.tntCardContainer.setOnClickListener {
            val action = TaskListFragmentDirections
                .actionTaskListToTaskEditFragment(task = item)
            itemView.safeNavigateTo(action = action)
        }
    }

    override fun onBind(item: Task) {
        super.onBind(item)
        with(binding) {
            tntTextTitle.text = item.title
            tntTextDate.text = item.date
            tntTextTime.text = item.time
            tntCardContainer.setColor(item.colorTag)
            tntImageState.setChecked(item.isDone)
        }
    }
}