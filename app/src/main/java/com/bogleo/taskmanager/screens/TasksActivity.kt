package com.bogleo.taskmanager.screens

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.common.NOTIFICATION_TASK_EXTRA
import com.bogleo.taskmanager.common.NotificationHelper
import com.bogleo.taskmanager.common.Utils
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.databinding.ActivityTasksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksActivity : AppCompatActivity() {

    private val viewModel: TasksViewModel by viewModels()
    private lateinit var binding: ActivityTasksBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksBinding.inflate(layoutInflater)



        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onResume() {
        super.onResume()
        val task = intent.getParcelableExtra<Task>(NOTIFICATION_TASK_EXTRA)
        if (task != null) {
            viewModel.updateTask(
                task = Utils.changeTask(
                    task = task,
                    isDone = true
                )
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
}