package com.bogleo.taskmanager.screens

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.bogleo.taskmanager.R
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.databinding.ActivityTasksBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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

        // TODO remove this
        //CoroutineScope(Job() + Dispatchers.IO).launch {
        //    viewModel.addTask(Task(
        //        id = 0,
        //        title = "Take poop",
        //        date = "20.20.22",
        //        time = "20:20",
        //        tags = "tags",
        //        colorTag = 0,
        //        isDone = false
        //    ))
        //}
        //viewModel.readAllData.observe(this) {
        //    Log.e("Activity", it[0].title)
        //}

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}