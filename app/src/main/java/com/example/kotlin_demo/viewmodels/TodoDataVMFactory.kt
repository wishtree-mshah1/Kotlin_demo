package com.example.kotlin_demo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin_demo.repo.TodoDataRepository

class TodoDataVMFactory(private val repository: TodoDataRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoDataViewModels(repository) as T
    }

}