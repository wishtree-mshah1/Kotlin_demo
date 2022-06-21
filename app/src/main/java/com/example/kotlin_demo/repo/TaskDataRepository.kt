package com.example.kotlin_demo.repo

import androidx.lifecycle.LiveData
import com.example.kotlin_demo.data.TaskDataDao
import com.example.kotlin_demo.data.Temp

class TaskDataRepository(private val taskDataDao: TaskDataDao) {

    val allData1: LiveData<List<Temp>> = taskDataDao.getAllApiData()

    suspend fun insert(temp: Temp){
        taskDataDao.insert(temp)
    }

    suspend fun delete(temp: Temp){
        taskDataDao.delete(temp)
    }
    suspend fun insertAll(temp: Temp){
        taskDataDao.insertAll(temp)
    }

}