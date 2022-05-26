package com.example.kotlin_demo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_demo.data.MyDataItem
import com.example.kotlin_demo.repo.APiDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApiDataViewModels(private val repository: APiDataRepository): ViewModel() {

    val allData: LiveData<List<MyDataItem>>
        get() = repository.allData

    fun insertData(myDataItem: MyDataItem){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(myDataItem)
        }
    }

}