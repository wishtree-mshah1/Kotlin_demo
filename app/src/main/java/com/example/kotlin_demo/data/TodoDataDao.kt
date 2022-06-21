package com.example.kotlin_demo.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todoData: TodoData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(todoData: TodoData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(todoData: TodoData)

    @Delete
    suspend fun delete(todoData: TodoData)

    @Query(value = "DELETE FROM todo_table WHERE id = :id")
    suspend fun deleteId( id: Int )


    @Query(value = "SELECT * From todo_table order by id ASC")
    fun getAllApiData():LiveData<List<TodoData>>
}

