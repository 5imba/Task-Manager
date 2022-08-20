package com.bogleo.taskmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bogleo.taskmanager.common.NOTIFICATION_CHANNEL_DEFAULT
import com.bogleo.taskmanager.screens.TasksActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_DEFAULT,
            getString(R.string.notification_title),
            NotificationManager.IMPORTANCE_DEFAULT
        )
            .apply {
                description = getString(R.string.notification_description)
            }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(this, TasksActivity::class.java)
        startActivity(intent)
    }
}