package com.bogleo.taskmanager.di

import android.content.Context
import androidx.room.Room
import com.bogleo.taskmanager.database.TaskDao
import com.bogleo.taskmanager.database.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideTaskDB(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, TaskDatabase::class.java, "task_database")
            .build()

    @Provides
    @Singleton
    fun provideTaskDao(taskDatabase: TaskDatabase): TaskDao =
        taskDatabase.taskDao()
}