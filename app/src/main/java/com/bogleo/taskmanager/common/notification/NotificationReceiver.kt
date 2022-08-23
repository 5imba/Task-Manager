package com.bogleo.taskmanager.common.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bogleo.taskmanager.MainActivity
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.common.NOTIFICATION_CHANNEL_DEFAULT
import com.bogleo.taskmanager.common.NOTIFICATION_TASK_EXTRA
import com.bogleo.taskmanager.model.Task

private const val TAG = "NotificationService"

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val task = intent.getParcelableExtra<Task>(NOTIFICATION_TASK_EXTRA)
        if (task != null) {

            val setDoneIntent = Intent(context, MainActivity::class.java)
                .putExtra(NOTIFICATION_TASK_EXTRA, task)
            val setDonePendingIntent = PendingIntent.getActivity(
                context,
                task.id.toInt(),
                setDoneIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_DEFAULT)
                .setContentTitle(task.title)
                .setContentText(context.getString(R.string.notification_title))
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setOnlyAlertOnce(true)
                //.setWhen(task.timeMillis)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .addAction(
                    NotificationCompat.Action.Builder(
                        R.drawable.ic_tick,
                        context.getString(R.string.set_complete),
                        setDonePendingIntent
                    )
                        .setContextual(false)
                        .setSemanticAction(NotificationCompat.Action.SEMANTIC_ACTION_MARK_AS_READ)
                        .setShowsUserInterface(false)
                        .build()
                )
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(task.title)
                )
                .extend(
                    NotificationCompat.WearableExtender()
                        .setContentIntentAvailableOffline(false)
                )
                .build()

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(task.id.toInt(), notification)

            Log.i(TAG, "Task notification schedule: ${task.title}")
        }
    }
}