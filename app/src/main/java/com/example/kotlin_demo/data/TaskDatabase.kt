package com.example.kotlin_demo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MyDataItem::class, TodoData::class, Temp::class], version = 1, exportSchema = true)

abstract class TaskDatabase: RoomDatabase() {

    abstract fun getTaskDataDao(): TaskDataDao
    abstract fun getApiDataDao(): ApiDataDao
    abstract fun getTodoDataDao(): TodoDataDao

    companion object{

        //volatile tells all thread about its updated value
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context):TaskDatabase{
            return INSTANCE ?: synchronized(this){
                val  instance = Room.databaseBuilder(
                    context,
                    TaskDatabase::class.java,
                    "tododata_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}