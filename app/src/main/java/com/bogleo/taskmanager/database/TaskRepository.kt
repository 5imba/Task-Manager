package com.bogleo.taskmanager.database

import androidx.lifecycle.LiveData
import com.bogleo.taskmanager.data.Task
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    val readAllData: LiveData<List<Task>> = taskDao.readAllData()

    suspend fun addTask(task: Task): Long{
        return taskDao.addTask(task)
    }

    suspend fun updateTask(task: Task){
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task){
        taskDao.deleteTask(task)
    }

    suspend fun deleteAllTasks(){
        taskDao.deleteAllTasks()
    }

    fun searchTask(searchQuery: String): LiveData<List<Task>> {
        return taskDao.searchTask(searchQuery)
    }

}