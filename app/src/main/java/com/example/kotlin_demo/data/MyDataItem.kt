package com.example.kotlin_demo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apidata_table")
data class MyDataItem(
    @ColumnInfo(name = "body") val body: String,
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title")val title: String,
    @ColumnInfo(name = "userid")val userId: Int
)