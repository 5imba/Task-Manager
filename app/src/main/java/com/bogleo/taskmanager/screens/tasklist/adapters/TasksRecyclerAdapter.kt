package com.bogleo.taskmanager.screens.tasklist.adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.common.DataListener
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.screens.tasklist.TaskListFragmentDirections
import javax.inject.Inject

private const val TAG = "TasksRecyclerAdapter"

class TasksRecyclerAdapter @Inject constructor() :
    RecyclerView.Adapter<TasksRecyclerAdapter.MyViewHolder>(),
    DataListener<List<Task>> {

    private var taskList: List<Task> = emptyList()
    var dataListener: DataListener<Task>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = taskList[position]
        bindUi(holder = holder, task = task)
        bindButtons(holder = holder, position = position)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDataChange(data: List<Task>) {
        this.taskList = data
        notifyDataSetChanged()
    }

    private fun changeTaskState(position: Int) {
        val task = taskList[position]
        val newTask = Task(id = task.id, title = task.title, date = task.date, time = task.time,
            tags = task.tags, colorTag = task.colorTag, isDone = !task.isDone)

        dataListener?.onDataChange(newTask)
        notifyItemChanged(position)
    }

    private fun bindUi(holder: MyViewHolder, task: Task) {
        // Task info
        holder.containerConstraint.backgroundTintList = ColorStateList.valueOf(task.colorTag)
        holder.taskTitleTextView.text = task.title
        holder.dateTextView.text = task.date
        holder.timeTextView.text = task.time

        // Task state
        holder.taskStateImageView.setImageResource(
            if(task.isDone) R.drawable.ic_tick
            else R.drawable.ic_circle_empty
        )

        // Tags Recycler binding
        val adapter = TagsRecyclerAdapter()
        holder.tagsRecyclerView.adapter = adapter
        holder.tagsRecyclerView.layoutManager = LinearLayoutManager(
            holder.itemView.context,
            RecyclerView.HORIZONTAL,
            false
        )

        // Set tags data
        val tags = task.tags.split(',')
        adapter.onDataChange(tags.filter { tag -> tag.trim().isNotEmpty() })
    }

    private fun bindButtons(holder: MyViewHolder, position: Int) {
        // Switch task state
        holder.taskStateImageView.setOnClickListener {
            changeTaskState(position = position)
        }

        // Navigate to view task
        holder.containerConstraint.setOnClickListener {
            try {
                val action = TaskListFragmentDirections
                    .actionTaskListToTaskView(task = taskList[position])
                holder.itemView.findNavController().navigate(action)
            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.localizedMessage}}")
            }
        }

        // Navigate to edit task
        holder.taskEditImageView.setOnClickListener {
            try {
                val action = TaskListFragmentDirections
                    .actionTaskListToTaskEditFragment(task = taskList[position])
                holder.itemView.findNavController().navigate(action)
            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.localizedMessage}}")
            }
        }
    }


    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val containerConstraint: ConstraintLayout = itemView.findViewById(R.id.containerConstraintTi)
        val taskTitleTextView: TextView = itemView.findViewById(R.id.taskTitleTextTi)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextTi)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextTi)
        val taskEditImageView: ImageView = itemView.findViewById(R.id.taskEditImageTi)
        val taskStateImageView: ImageView = itemView.findViewById(R.id.taskStateImageTi)
        val tagsRecyclerView: RecyclerView = itemView.findViewById(R.id.tagsRecyclerTi)
    }
}