package com.bogleo.taskmanager.screens.browsing

import android.content.Context
import androidx.lifecycle.ViewModel
import com.bogleo.taskmanager.common.notification.NotificationHelper
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.screens.MainViewModel

class TaskBrowsingViewModel : ViewModel() {

    lateinit var mainViewModel: MainViewModel

    fun switchTaskComplete(task: Task, context: Context, onTaskUpdated: (() -> Unit)? = null) {
        val newTask = task.copy(isDone = !task.isDone)
        mainViewModel.updateTask(task = newTask) {
            NotificationHelper.setTaskNotification(
                context = context,
                task = newTask
            )
            onTaskUpdated?.let { callback -> callback() }
        }
    }

}