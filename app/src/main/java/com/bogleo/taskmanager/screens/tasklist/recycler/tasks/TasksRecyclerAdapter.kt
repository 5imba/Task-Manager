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
import javax.inject.Inject

private const val TAG = "TasksRecycler"

class TasksRecyclerAdapter @Inject constructor() :
    RecyclerView.Adapter<TasksRecyclerAdapter.MyViewHolder>()
{

    @Inject
    lateinit var tagsRecyclerAdapter: TagsRecyclerAdapter
    private var dataListener: DataListener<Task>? = null
    private val mTaskList: MutableList<Task> = ArrayList()

    fun setOnDataChangeListener(dataListener: DataListener<Task>) {
        this.dataListener = dataListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = mTaskList[position]
        bindUi(holder = holder, task = task)
        bindButtons(holder = holder, position = position)
    }

    override fun getItemCount(): Int {
        return mTaskList.size
    }

    fun getData(): List<Task> = mTaskList
    fun setData(data: List<Task>) {
        mTaskList.clear()
        mTaskList.addAll(data)
    }

    private fun changeTaskState(position: Int) {
        val task = mTaskList[position]
        val newTask = task.change(isDone = !task.isDone)

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
        holder.tagsRecyclerView.adapter = tagsRecyclerAdapter
        holder.tagsRecyclerView.layoutManager = LinearLayoutManager(
            holder.itemView.context,
            RecyclerView.HORIZONTAL,
            false
        )
        // Set tags data
        val tags = task.tags.split(',')
        val tagsList = tags.filter { tag -> tag.trim().isNotEmpty() }
        tagsRecyclerAdapter.setData(data = tagsList)
    }

    private fun bindButtons(holder: MyViewHolder, position: Int) {
        // Switch task state
        holder.taskStateImageView.setOnClickListener {
            changeTaskState(position = position)
        }

        // Navigate to view task
        holder.containerConstraint.setOnClickListener {
            val action = TaskListFragmentDirections
                .actionTaskListToTaskView(task = mTaskList[position])
            navigateTo(itemView = holder.itemView, action = action)
        }

        // Navigate to edit task
        holder.taskEditImageView.setOnClickListener {
            val action = TaskListFragmentDirections
                .actionTaskListToTaskEditFragment(task = mTaskList[position])
            navigateTo(itemView = holder.itemView, action = action)
        }
    }

    private fun navigateTo(itemView: View, action: NavDirections) {
        try {
            itemView.findNavController().navigate(action)
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.localizedMessage}")
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