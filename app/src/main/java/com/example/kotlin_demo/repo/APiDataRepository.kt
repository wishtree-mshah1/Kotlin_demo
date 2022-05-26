package com.example.kotlin_demo.repo

import androidx.lifecycle.LiveData
import com.example.kotlin_demo.data.ApiDataDao
import com.example.kotlin_demo.data.MyDataItem

class APiDataRepository(private val apiDataDao: ApiDataDao) {

    val allData: LiveData<List<MyDataItem>> = apiDataDao.getAllApiData()

    suspend fun insert(myDataItem: MyDataItem){
        apiDataDao.insert(myDataItem)
    }

    suspend fun insertAll(myDataItem: MyDataItem){
        apiDataDao.insertAll(myDataItem)
    }

}