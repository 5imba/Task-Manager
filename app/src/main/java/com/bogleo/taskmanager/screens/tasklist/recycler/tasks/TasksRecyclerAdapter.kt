package com.bogleo.taskmanager.screens.tasklist.recycler.tasks

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.common.DataListener
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.data.change
import com.bogleo.taskmanager.screens.tasklist.TaskListFragmentDirections
import com.bogleo.taskmanager.screens.tasklist.recycler.tags.TagsRecyclerAdapter
import java.lang.IllegalArgumentException

private const val TAG = "TasksRecycler"

class TasksRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private val mTaskList: MutableList<Task> = ArrayList()
    private var dataListener: DataListener<Task>? = null

    fun setOnDataChangeListener(dataListener: DataListener<Task>) {
        this.dataListener = dataListener
    }

    fun getData(): List<Task> = mTaskList
    fun setData(data: List<Task>) {
        mTaskList.clear()
        mTaskList.addAll(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = when(viewType) {
        R.layout.item_task -> TaskViewHolder(itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false))
        else -> throw IllegalArgumentException("Illegal type: $viewType")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TaskViewHolder -> holder.onBind(mTaskList[position]) { dataListener?.onDataChange(it) }
        }
    }

    override fun getItemCount(): Int {
        return mTaskList.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_task
    }

    class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val containerConstraint: ConstraintLayout = itemView.findViewById(R.id.containerConstraintTi)
        private val taskTitleTextView: TextView = itemView.findViewById(R.id.taskTitleTextTi)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextTi)
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTextTi)
        private val taskEditImageView: ImageView = itemView.findViewById(R.id.taskEditImageTi)
        private val taskStateImageView: ImageView = itemView.findViewById(R.id.taskStateImageTi)
        private val tagsRecyclerView: RecyclerView = itemView.findViewById(R.id.tagsRecyclerTi)

        fun onBind(task: Task, dateChangeCallback: (task: Task) -> Unit) {
            // Task info
            containerConstraint.backgroundTintList = ColorStateList.valueOf(task.colorTag)
            taskTitleTextView.text = task.title
            dateTextView.text = task.date
            timeTextView.text = task.time

            // Task state
            taskStateImageView.setImageResource(
                if(task.isDone) R.drawable.ic_tick
                else R.drawable.ic_circle_empty
            )

            // Tags Recycler binding
            val tagsRecyclerAdapter = TagsRecyclerAdapter()
            tagsRecyclerView.adapter = tagsRecyclerAdapter
            tagsRecyclerView.layoutManager = LinearLayoutManager(
                itemView.context,
                RecyclerView.HORIZONTAL,
                false
            )
            // Set tags data
            val tags = task.tags.split(',')
            val tagsList = tags.filter { tag -> tag.trim().isNotEmpty() }
            tagsRecyclerAdapter.setData(data = tagsList)

            // Switch task state
            taskStateImageView.setOnClickListener {
                val newTask = task.change(isDone = !task.isDone)
                dateChangeCallback(newTask)
            }

            // Navigate to view task
            containerConstraint.setOnClickListener {
                val action = TaskListFragmentDirections
                    .actionTaskListToTaskView(task = task)
                navigateTo(itemView = itemView, action = action)
            }

            // Navigate to edit task
            taskEditImageView.setOnClickListener {
                val action = TaskListFragmentDirections
                    .actionTaskListToTaskEditFragment(task = task)
                navigateTo(itemView = itemView, action = action)
            }
        }

        private fun navigateTo(itemView: View, action: NavDirections) {
            try {
                itemView.findNavController().navigate(action)
            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.localizedMessage}")
            }
        }
    }
}