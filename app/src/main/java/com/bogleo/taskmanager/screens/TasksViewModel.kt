package com.bogleo.taskmanager.screens

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val readAllData: LiveData<List<Task>> = repository.readAllData()

    suspend fun addTask(task: Task): Long{
        return repository.addTask(task)
    }

    fun editTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            repository.editTask(task)
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
        }
    }

    fun deleteAllTasks(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTasks()
        }
    }

    fun searchTask(searchQuery: String): LiveData<List<Task>> {
        return repository.searchTask(searchQuery)
    }
}