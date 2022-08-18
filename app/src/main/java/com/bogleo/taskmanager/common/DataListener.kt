package com.bogleo.taskmanager.common

interface DataListener<in T> {
    fun onDataChange(data: T)
}