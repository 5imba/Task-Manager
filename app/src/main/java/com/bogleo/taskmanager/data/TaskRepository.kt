package com.bogleo.taskmanager.data

import android.util.Log
import androidx.lifecycle.LiveData
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    fun readAllData(): LiveData<List<Task>> {
        Log.e("readAllData", "Data Read")
        return taskDao.readAllData()
    }

    suspend fun addTask(task: Task): Long{
        return taskDao.addTask(task)
    }

    suspend fun editTask(task: Task){
        taskDao.editTask(task)
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