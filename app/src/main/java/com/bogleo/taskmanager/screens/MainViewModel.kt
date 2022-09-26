package com.bogleo.taskmanager.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogleo.taskmanager.data.Task
import com.bogleo.taskmanager.database.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val readAllData: LiveData<List<Task>> = repository.readAllData

    fun addTask(task: Task, callback: (id: Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = repository.addTask(task)
            launch(Dispatchers.Main) { callback(id) }
        }
    }

    fun updateTask(task: Task, callback: () -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
            launch(Dispatchers.Main) { callback() }
        }
    }

    fun deleteTask(task: Task, callback: () -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
            launch(Dispatchers.Main) { callback() }
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