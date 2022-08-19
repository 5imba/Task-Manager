package com.bogleo.taskmanager.screens.tasklist.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.common.DataListener

class TagsRecyclerAdapter: RecyclerView.Adapter<TagsRecyclerAdapter.MyViewHolder>(),
    DataListener<List<String>> {

    private var tagsList = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tag, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tagTextView.text = tagsList[position].trim()
    }

    override fun getItemCount(): Int {
        return tagsList.size
    }

    // TODO create data insert/delete/updated system
    @SuppressLint("NotifyDataSetChanged")
    override fun onDataChange(data: List<String>) {
        this.tagsList = data
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tagTextView: TextView = itemView.findViewById(R.id.tagTextIt)
    }
}