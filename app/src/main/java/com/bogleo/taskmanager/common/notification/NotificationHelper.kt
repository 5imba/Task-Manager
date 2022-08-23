package com.bogleo.taskmanager.common.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.bogleo.taskmanager.common.NOTIFICATION_TASK_EXTRA
import com.bogleo.taskmanager.model.Task

object NotificationHelper {

    fun setTaskNotification(context: Context, task: Task) {
        if (task.isDone) {
            removeNotification(
                context = context,
                task = task
            )
        } else {
            scheduleNotification(
                context = context,
                task = task
            )
        }
    }

    fun scheduleNotification(context: Context, task: Task) {
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra(NOTIFICATION_TASK_EXTRA, task)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            task.timeMillis,
            pendingIntent
        )
    }

    fun removeNotification(context: Context, task: Task) {
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra(NOTIFICATION_TASK_EXTRA, task)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}