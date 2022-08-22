package com.bogleo.taskmanager.data

interface DataListener<in T> {
    fun onDataChange(data: T)
}