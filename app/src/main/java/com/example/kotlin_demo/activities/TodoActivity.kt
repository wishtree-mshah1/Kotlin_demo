package com.example.kotlin_demo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_demo.R
import com.example.kotlin_demo.adapters.TodoAdapter
import com.example.kotlin_demo.data.TodoData
import com.example.kotlin_demo.data.TodoDatabase
import com.example.kotlin_demo.repo.TodoDataRepository
import com.example.kotlin_demo.viewmodels.TodoDataVMFactory
import com.example.kotlin_demo.viewmodels.TodoDataViewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

private const val TIME_INTERVAL =2000 // # milliseconds, desired time passed between two back presses.

private var mBackPressed: Long = 0

class TodoActivity : AppCompatActivity() {

    lateinit var bottom_nav: BottomNavigationView
    lateinit var floating_btn: ExtendedFloatingActionButton
    private lateinit var todoDataViewModels: TodoDataViewModels
    override fun onBackPressed() {
            mBackPressed = System.currentTimeMillis();
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)
        bottom_nav = findViewById(R.id.bottom_navigation)
        floating_btn = findViewById(R.id.floating_btn)


        val recyclerview = findViewById<RecyclerView>(R.id.todo_recycler)
        recyclerview.layoutManager = LinearLayoutManager(this)

        val adapter = TodoAdapter(this,this)
        recyclerview.adapter = adapter

        val dao = TodoDatabase.getDatabase(applicationContext).getTodoDataDao()
        val repository = TodoDataRepository(dao)
        todoDataViewModels = ViewModelProvider(this, TodoDataVMFactory(repository)).get(
            TodoDataViewModels::class.java)
        todoDataViewModels.allData.observe(this, Observer {
            adapter.updateTodoData(it)
        })
        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
        floating_btn.setOnClickListener(){
            intent = Intent(applicationContext, TodoAddActivity::class.java)
            startActivity(intent)
        }



        bottom_nav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.api_data ->{
                    Toast.makeText(applicationContext,"GET API DATA",Toast.LENGTH_SHORT).show()
                    intent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(intent)
                }
                R.id.todo ->{
                    Toast.makeText(applicationContext,"TODO",Toast.LENGTH_SHORT).show()
                    intent = Intent(applicationContext, TodoActivity::class.java)
                    startActivity(intent)
                }
                R.id.profile ->{
                    Toast.makeText(applicationContext,"3", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }

    fun setClick(todoData: TodoData) {
        todoDataViewModels.deleteData(todoData)
        println("sdhfhsdfsdfg////")

    }

    fun onItemClick(todoData: TodoData) {
        val intent =Intent(this,TodoAddActivity::class.java)

        intent.putExtra("title",todoData.title)
        intent.putExtra("desc",todoData.desc)
        intent.putExtra("id1",todoData.id)
        intent.putExtra("color",todoData.color)
        intent.putExtra("update_task","update")
        startActivity(intent)
        this.finish()
    }
}


