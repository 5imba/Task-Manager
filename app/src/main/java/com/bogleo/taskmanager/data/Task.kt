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
    val tags: String,
    val colorTag: Int,
    val isDone: Boolean
): Parcelable

fun Task.change(
    id: Long? = null,
    title: String? = null,
    date: String? = null,
    time: String? = null,
    timeMillis: Long? = null,
    tags: String? = null,
    colorTag: Int? = null,
    isDone: Boolean? = null
): Task {
    return Task(
        id = id?: this.id,
        title = title?: this.title,
        date = date?: this.date,
        time = time?: this.time,
        timeMillis = timeMillis?: this.timeMillis,
        tags = tags?: this.tags,
        colorTag = colorTag?: this.colorTag,
        isDone = isDone?: this.isDone
    )
}