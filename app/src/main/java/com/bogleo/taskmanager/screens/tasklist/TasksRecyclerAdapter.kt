package com.bogleo.taskmanager.screens.tasklist

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.common.DataListener
import com.bogleo.taskmanager.data.Task

class TasksRecyclerAdapter: RecyclerView.Adapter<TasksRecyclerAdapter.MyViewHolder>(),
    DataListener<List<Task>> {

    private var taskList: List<Task> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = taskList[position]

        holder.containerConstraint.backgroundTintList = ColorStateList.valueOf(task.colorTag)
        holder.timeTextView.text = task.title
        holder.dateTextView.text = task.date
        holder.timeTextView.text = task.time
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onDataChange(data: List<Task>) {
        this.taskList = data
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val containerConstraint: ConstraintLayout = itemView.findViewById(R.id.containerConstraintTi)
        val taskTitleTextView: TextView = itemView.findViewById(R.id.taskTitleTextTi)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextTi)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextTi)
        val taskStateImageView: ImageView = itemView.findViewById(R.id.taskStateImageTi)
    }
}