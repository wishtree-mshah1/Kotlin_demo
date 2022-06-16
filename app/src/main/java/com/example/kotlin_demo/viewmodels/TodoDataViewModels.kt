package com.example.kotlin_demo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_demo.data.TodoData
import com.example.kotlin_demo.repo.TodoDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoDataViewModels(private val repository: TodoDataRepository): ViewModel() {

    val allData: LiveData<List<TodoData>>
        get() = repository.allData

    fun insertData(todoData: TodoData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(todoData)
        }
    }

    fun deleteData(todoData: TodoData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(todoData)
        }
    }

    fun deleteDataId(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteId(id)
        }
    }



}