package com.example.kotlin_demo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TodoData::class], version = 1, exportSchema = false)

abstract class TodoDatabase: RoomDatabase() {

    abstract fun getTodoDataDao(): TodoDataDao

    companion object{

        //volatile tells all thread about its updated value
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getDatabase(context: Context):TodoDatabase{
            return INSTANCE ?: synchronized(this){
                val  instance = Room.databaseBuilder(
                    context,
                    TodoDatabase::class.java,
                    "tododata_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}