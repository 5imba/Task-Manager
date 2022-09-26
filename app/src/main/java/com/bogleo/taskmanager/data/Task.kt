package com.bogleo.taskmanager.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "task_table")
data class Task (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val date: String,
    val time: String,
    val timeMillis: Long,
    val tags: List<String>,
    val colorTag: Int,
    val isDone: Boolean
): Parcelable