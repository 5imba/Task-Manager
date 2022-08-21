package com.bogleo.taskmanager.screens.tasklist.recycler.tags

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bogleo.taskmanager.R
import javax.inject.Inject

class TagsRecyclerAdapter @Inject constructor() :
    RecyclerView.Adapter<TagsRecyclerAdapter.TagsViewHolder>() {

    private var mTagsList: MutableList<String> = ArrayList()

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

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<String>) {
        mTagsList.clear()
        mTagsList.addAll(data)
        notifyDataSetChanged()
    }

    class TagsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tagTextView: TextView = itemView.findViewById(R.id.tagTextIt)
    }
}