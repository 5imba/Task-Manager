package com.bogleo.taskmanager.screens.tasklist.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.common.DataListener

class TagsRecyclerAdapter: RecyclerView.Adapter<TagsRecyclerAdapter.TagsViewHolder>(),
    DataListener<List<String>> {

    private var mTagsList = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsViewHolder {
        return TagsViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tag, parent, false))
    }

    override fun onBindViewHolder(holder: TagsViewHolder, position: Int) {
        holder.tagTextView.text = mTagsList[position].trim()
    }

    override fun getItemCount(): Int {
        return mTagsList.size
    }

    // TODO create data insert/delete/updated system
    @SuppressLint("NotifyDataSetChanged")
    override fun onDataChange(data: List<String>) {
        this.mTagsList = data
        notifyDataSetChanged()
    }

    class TagsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tagTextView: TextView = itemView.findViewById(R.id.tagTextIt)
    }
}