package com.example.kotlin_demo.repo

import androidx.lifecycle.LiveData
import com.example.kotlin_demo.data.TodoData
import com.example.kotlin_demo.data.TodoDataDao

class TodoDataRepository(private val todoDataDao: TodoDataDao) {

    val allData: LiveData<List<TodoData>> = todoDataDao.getAllApiData()

    suspend fun insert(todoData: TodoData){
        todoDataDao.insert(todoData)
    }
    suspend fun update(todoData: TodoData){
        todoDataDao.update(todoData)
    }

    suspend fun delete(todoData: TodoData){
        todoDataDao.delete(todoData)
    }
    suspend fun insertAll(todoData: TodoData){
        todoDataDao.insertAll(todoData)
    }

    suspend fun deleteId(id: Int) {
        todoDataDao.deleteId(id)
    }

}