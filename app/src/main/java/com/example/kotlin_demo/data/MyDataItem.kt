package com.example.kotlin_demo.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import retrofit2.http.Url
import java.net.URI

@Entity(tableName = "apidata_table")
data class MyDataItem(
    @ColumnInfo(name = "body") val body: String,
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title")val title: String,
    @ColumnInfo(name = "userid")val userId: Int
)

@Entity(tableName = "todo_table")
data class TodoData(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "desc") val desc: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "image") val image: String,
    val color: String,
    val hour: String,
    val min: String,
    val ampm: String,
    var selected: Boolean,


)