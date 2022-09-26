package com.bogleo.taskmanager.common.adapters.recycler.shells

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.common.adapters.recycler.TaskShell
import com.bogleo.taskmanager.common.adapters.recycler.BaseViewHolder
import com.bogleo.taskmanager.common.safeNavigateTo
import com.bogleo.taskmanager.common.setChecked
import com.bogleo.taskmanager.common.setColor
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.databinding.ItemTaskNoTagsBinding
import com.bogleo.taskmanager.screens.listmain.TaskListFragmentDirections

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
        binding.root.setOnClickListener {
            val action = TaskListFragmentDirections
                .actionTaskListToTaskBrowsing(task = item)
            itemView.safeNavigateTo(action = action)
        }
        // Navigate to edit task
        binding.tntImageEdit.setOnClickListener {
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