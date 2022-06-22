package com.example.kotlin_demo.data

import android.widget.CheckBox
import androidx.room.*
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

@Entity(tableName = "task_table",

        foreignKeys = arrayOf(ForeignKey(
            entity = TodoData::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("todo_id"),
            onDelete = ForeignKey.CASCADE

        ))
)
data class Temp(
    @ColumnInfo val todo_id: Long,
    @PrimaryKey(autoGenerate = true) val task_id: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "isselected") val isselected: Boolean
)