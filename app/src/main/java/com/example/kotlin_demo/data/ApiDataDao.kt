package com.example.kotlin_demo.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ApiDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(myDataItem: MyDataItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(myDataItem: MyDataItem)

    @Query(value = "SELECT * From apidata_table order by id ASC")
    fun getAllApiData():LiveData<List<MyDataItem>>
}

