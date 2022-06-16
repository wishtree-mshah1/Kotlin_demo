package com.example.kotlin_demo.activities

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.example.kotlin_demo.databinding.ActivityMainBinding
import com.example.kotlin_demo.notification.channelID
import com.example.kotlin_demo.repo.TodoDataRepository
import com.example.kotlin_demo.viewmodels.TodoDataVMFactory
import com.example.kotlin_demo.viewmodels.TodoDataViewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

private const val TIME_INTERVAL =2000 // # milliseconds, desired time passed between two back presses.

private var mBackPressed: Long = 0

class TodoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var bottom_nav: BottomNavigationView
    lateinit var floating_btn: ExtendedFloatingActionButton
    private lateinit var todoDataViewModels: TodoDataViewModels
    private var mainMenu: Menu? = null
    private lateinit var adapter: TodoAdapter


    override fun onBackPressed() {
        mBackPressed = System.currentTimeMillis();
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_todo)
        bottom_nav = findViewById(R.id.bottom_navigation)
        floating_btn = findViewById(R.id.floating_btn)

        createNotificationChannel()

        val recyclerview = findViewById<RecyclerView>(R.id.todo_recycler)
        recyclerview.layoutManager = LinearLayoutManager(this)

        adapter = TodoAdapter(this,this){show -> showDeleteMenu(show)}
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

    fun showDeleteMenu(show: Boolean){
        mainMenu?.findItem(R.id.delete_all)?.isVisible = show
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        mainMenu = menu
        menuInflater.inflate(R.menu.custom_menu,mainMenu)
        showDeleteMenu(false)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.delete_all ->{delete()}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun delete() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Delete")
        alertDialog.setMessage("Do you want to delete all selected items?")
        alertDialog.setPositiveButton("Delete"){_,_ ->
            adapter.deleteSelectedItems()
            showDeleteMenu(false)
        }
        alertDialog.setNegativeButton("Cancel"){_,_ ->}
        alertDialog.show()
    }

    private fun createNotificationChannel() {
        val name = "Notif Channel"
        val des = "Description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(channelID,name, importance)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        channel.description = des

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
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