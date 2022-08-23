package com.bogleo.taskmanager.adapters.recycler.shells

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.adapters.recycler.TaskShell
import com.bogleo.taskmanager.adapters.recycler.BaseViewHolder
import com.bogleo.taskmanager.adapters.recycler.TagsRecyclerAdapter
import com.bogleo.taskmanager.common.Extensions.safeNavigateTo
import com.bogleo.taskmanager.common.Extensions.setChecked
import com.bogleo.taskmanager.common.Extensions.setColor
import com.bogleo.taskmanager.model.Task
import com.bogleo.taskmanager.databinding.ItemTaskTagsBinding
import com.bogleo.taskmanager.screens.tasklist.TaskListFragmentDirections

class TaskWithTagsShell(
    private val onStateChange: (Task) -> Unit
) : TaskShell<ItemTaskTagsBinding> {

    override fun isRelativeItem(task: Task) = task.tags.isNotEmpty()

    override fun getLayoutId() = R.layout.item_task_tags

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemTaskTagsBinding, Task> {
        val binding = ItemTaskTagsBinding.inflate(layoutInflater, parent, false)
        return TaskWithTagsViewHolder(binding = binding, onStateChange = onStateChange)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldTask: Task, newTask: Task) = oldTask.id == newTask.id
        override fun areContentsTheSame(oldTask: Task, newTask: Task) = oldTask == newTask
    }
}

class TaskWithTagsViewHolder(
    binding: ItemTaskTagsBinding,
    val onStateChange: (Task) -> Unit
) : BaseViewHolder<ItemTaskTagsBinding, Task>(binding = binding) {

    init {
        // Switch task state
        binding.twtImageState.setOnClickListener {
            if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
            onStateChange(item.copy(isDone = !item.isDone))
        }
        // Navigate to view task
        binding.twtCardContainer.setOnClickListener {
            val action = TaskListFragmentDirections
                .actionTaskListToTaskView(task = item)
            itemView.safeNavigateTo(action = action)
        }
        // Navigate to edit task
        binding.twtImageEdit.setOnClickListener {
            val action = TaskListFragmentDirections
                .actionTaskListToTaskEditFragment(task = item)
            itemView.safeNavigateTo(action = action)
        }
    }

    override fun onBind(item: Task) {
        super.onBind(item)
        with(binding) {
            twtTextTitle.text = item.title
            twtTextDate.text = item.date
            twtTextTime.text = item.time
            twtCardContainer.setColor(item.colorTag)
            twtImageState.setChecked(item.isDone)
            twtRecyclerTags.setup(item.tags)
        }
    }

    private fun RecyclerView.setup(data: List<String>) {
        // Tags Recycler binding
        val tagsRecyclerAdapter = TagsRecyclerAdapter()
        adapter = tagsRecyclerAdapter
        layoutManager = LinearLayoutManager(
            itemView.context,
            RecyclerView.HORIZONTAL,
            false
        )
        // Set tags data
        tagsRecyclerAdapter.setData(data = data)
    }
}