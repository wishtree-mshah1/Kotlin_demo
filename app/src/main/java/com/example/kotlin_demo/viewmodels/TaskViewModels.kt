package com.example.kotlin_demo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_demo.data.Temp
import com.example.kotlin_demo.data.TodoData
import com.example.kotlin_demo.repo.TaskDataRepository
import com.example.kotlin_demo.repo.TodoDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModels(private val repository: TaskDataRepository): ViewModel() {

    val allData1: LiveData<List<Temp>>
        get() = repository.allData1

    fun insertData(temp: Temp){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(temp)
        }
    }

    fun deleteData(temp: Temp){
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(temp)
        }
    }

}