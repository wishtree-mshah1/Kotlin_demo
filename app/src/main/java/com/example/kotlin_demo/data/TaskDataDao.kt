package com.example.kotlin_demo.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(temp: Temp)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(temp: Temp)

    @Delete
    suspend fun delete(temp: Temp)


    @Query(value = "SELECT * From task_table order by task_id ASC")
    fun getAllApiData():LiveData<List<Temp>>
}

