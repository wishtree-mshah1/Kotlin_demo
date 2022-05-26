package com.example.kotlin_demo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin_demo.repo.APiDataRepository

class ApiDataVMFactory(private val repository: APiDataRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ApiDataViewModels(repository) as T
    }
}