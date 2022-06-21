package com.example.kotlin_demo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin_demo.repo.TaskDataRepository
import com.example.kotlin_demo.repo.TodoDataRepository

class TaskDataVMFactory(private val repository: TaskDataRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskViewModels(repository) as T
    }

}