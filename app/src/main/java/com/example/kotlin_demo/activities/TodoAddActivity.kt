package com.example.kotlin_demo.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin_demo.R
import com.example.kotlin_demo.data.TodoData
import com.example.kotlin_demo.data.TodoDatabase
import com.example.kotlin_demo.repo.TodoDataRepository
import com.example.kotlin_demo.viewmodels.TodoDataVMFactory
import com.example.kotlin_demo.viewmodels.TodoDataViewModels
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDateTime

class TodoAddActivity : AppCompatActivity() {

    private lateinit var submit_btn : Button
    private lateinit var update_btn : Button
    private lateinit var title : TextInputEditText
    private lateinit var desc : TextInputEditText
    private lateinit var todoDataViewModels: TodoDataViewModels
    var id: Long = 0
    var id1: Long = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_add)

        submit_btn =  findViewById(R.id.submit)
        update_btn =  findViewById(R.id.update)
        title = findViewById(R.id.title_txt)
        desc = findViewById(R.id.des_txt)

        val dao = TodoDatabase.getDatabase(applicationContext).getTodoDataDao()
        val repository = TodoDataRepository(dao)
        todoDataViewModels = ViewModelProvider(this, TodoDataVMFactory(repository)).get(TodoDataViewModels::class.java)
        todoDataViewModels.allData.observe(this, Observer {

        })

        val taskType = intent.getStringExtra("update_task")

        if (taskType.equals("update"))
        {
            update_btn.setVisibility(VISIBLE)
            submit_btn.setVisibility(INVISIBLE)
            val title_val = intent.getStringExtra("title")
            val desc_val =intent.getStringExtra("desc")
            id1 = intent.getLongExtra("id1",0)
            println("iddddddd")
            println(id1)
            title.setText(title_val)
            desc.setText(desc_val)


        }
        else{
            update_btn.setVisibility(INVISIBLE)
            submit_btn.setVisibility(VISIBLE)

            submit_btn.setText("Add Notes")
        }

        val current = LocalDateTime.now()
        var hours = current.hour
        if (hours > 12){
            hours -= 12
        }
        else{
            hours = hours
        }
        val minutes = current.minute
        val time = concat(hours,minutes)
        println(time)


        var date =  current.dayOfMonth
        var month = current.month
        var year = current.year
        val fulldate = date.toString()+"/"+month.toString()+"/"+year.toString()
        println("Date")
        println(fulldate)

        println(title.text.toString())

        submit_btn.setOnClickListener(){

            println("yeeeee")
            todoDataViewModels.insertData(TodoData(id,title.text.toString(),desc.text.toString(),time.toString(),fulldate.toString()))
            id = id+1
            intent = Intent(applicationContext, TodoActivity::class.java)
            startActivity(intent)
        }
        update_btn.setOnClickListener(){
            println("nooooooo")
            todoDataViewModels.insertData(TodoData(id1,title.text.toString(),desc.text.toString(),time.toString(),fulldate.toString()))
            intent = Intent(applicationContext, TodoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun concat(hours: Int, minutes: Int): String? {
        return hours.toString() +":"+ minutes.toString()
    }
}