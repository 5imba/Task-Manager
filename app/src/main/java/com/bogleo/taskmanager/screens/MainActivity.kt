package com.bogleo.taskmanager.screens

import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.common.NOTIFICATION_CHANNEL_DEFAULT
import com.bogleo.taskmanager.common.NOTIFICATION_TASK_EXTRA
import com.bogleo.taskmanager.common.notification.NotificationHelper
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        createNotificationChannel()
        setupNavController()
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        val task = intent.getParcelableExtra<Task>(NOTIFICATION_TASK_EXTRA)
        if (task != null && !task.isDone) {
            mViewModel.updateTask(
                task = task.copy(isDone = true)
            ) {
                NotificationHelper.removeNotification(
                    context = this,
                    task = task
                )
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun createNotificationChannel() {
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
    }

    private fun setupNavController() {
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}