package com.example.kotlin_demo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MyDataItem::class], version = 1, exportSchema = false)

abstract class ApiDatabase: RoomDatabase() {

    abstract fun getApiDataDao(): ApiDataDao

    companion object{

        //volatile tells all thread about its updated value
        @Volatile
        private var INSTANCE: ApiDatabase? = null

        fun getDatabase(context: Context):ApiDatabase{
            return INSTANCE ?: synchronized(this){
                val  instance = Room.databaseBuilder(
                    context,
                    ApiDatabase::class.java,
                    "apidata_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}