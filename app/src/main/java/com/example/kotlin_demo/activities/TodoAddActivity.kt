package com.example.kotlin_demo.activities

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin_demo.R
import com.example.kotlin_demo.data.TodoData
import com.example.kotlin_demo.data.TodoDatabase
import com.example.kotlin_demo.databinding.ActivityMainBinding
import com.example.kotlin_demo.notification.*
import com.example.kotlin_demo.repo.TodoDataRepository
import com.example.kotlin_demo.viewmodels.TodoDataVMFactory
import com.example.kotlin_demo.viewmodels.TodoDataViewModels
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import de.hdodenhof.circleimageview.CircleImageView
import java.time.LocalDateTime
import java.util.*


class TodoAddActivity : AppCompatActivity() {

    lateinit var previewSelectedTimeTextView: TextView
    lateinit var picker: MaterialTimePicker
    private lateinit var calender:Calendar
    var hours_alarm = 0
    var min_alarm = 0
    var ampm_alarm:String = "am"
    var title1:String = ""
    var des1:String = ""
    // listener which is triggered when the
    // time is picked from the time picker dialog

    private lateinit var submit_btn : Button
    private lateinit var update_btn : Button
    private lateinit var time_text : TextView
    private lateinit var time1 : TextView
    private lateinit var title : TextInputEditText
    private lateinit var desc : TextInputEditText
    private lateinit var desc_box : TextInputLayout
    private lateinit var todoDataViewModels: TodoDataViewModels
    private lateinit var blue_color: CircleImageView
    private lateinit var yellow_color: CircleImageView
    private lateinit var red_color: CircleImageView
    private lateinit var black_color: CircleImageView
    private lateinit var pink_color: CircleImageView
    private lateinit var img1: ImageView
    private lateinit var img2: ImageView
    private lateinit var img3: ImageView
    private lateinit var img4: ImageView
    private lateinit var img5: ImageView
    private lateinit var card_add: CardView
    private lateinit var binding: ActivityMainBinding
    private lateinit var alarmManager: AlarmManager
    private lateinit var reminderBrodcast: ReminderBrodcast
    private lateinit var pendingIntent: PendingIntent

    var id: Long = 0
    var id1: Long = 0
    var color: String = "Blue"


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_todo_add)
        createNotificationChannel()

        submit_btn =  findViewById(R.id.submit)
        update_btn =  findViewById(R.id.update)
        title = findViewById(R.id.title_txt)
        time1 = findViewById(R.id.time1)
        desc = findViewById(R.id.des_txt)
        desc_box = findViewById(R.id.description)
        blue_color = findViewById(R.id.blue_circle)
        yellow_color = findViewById(R.id.yellow_circle)
        red_color = findViewById(R.id.red_circle)
        black_color = findViewById(R.id.black_circle)
        pink_color = findViewById(R.id.pink_circle)
        card_add = findViewById(R.id.card_add)
        img1 = findViewById(R.id.img1)
        img2 = findViewById(R.id.img2)
        img3 = findViewById(R.id.img3)
        img4 = findViewById(R.id.img4)
        img5 = findViewById(R.id.img5)
        time_text = findViewById(R.id.timepick)

        time_text.setOnClickListener {

            showTimePicker()


        }

        blue_color.setOnClickListener(){
            color = "Blue"
            card_add.setBackgroundResource(R.color.blue)
            img1.setVisibility(VISIBLE)
            img2.setVisibility(INVISIBLE)
            img3.setVisibility(INVISIBLE)
            img4.setVisibility(INVISIBLE)
            img5.setVisibility(INVISIBLE)
        }
        yellow_color.setOnClickListener(){
            color = "Yellow"
            card_add.setBackgroundResource(R.color.yellow)
            img2.setVisibility(VISIBLE)
            img1.setVisibility(INVISIBLE)
            img3.setVisibility(INVISIBLE)
            img4.setVisibility(INVISIBLE)
            img5.setVisibility(INVISIBLE)
        }
        red_color.setOnClickListener(){
            color = "Red"
            card_add.setBackgroundResource(R.color.red)
            img3.setVisibility(VISIBLE)
            img2.setVisibility(INVISIBLE)
            img1.setVisibility(INVISIBLE)
            img4.setVisibility(INVISIBLE)
            img5.setVisibility(INVISIBLE)
        }
        black_color.setOnClickListener(){
            color = "Black"
            card_add.setBackgroundResource(R.color.black)
            img4.setVisibility(VISIBLE)
            img2.setVisibility(INVISIBLE)
            img3.setVisibility(INVISIBLE)
            img1.setVisibility(INVISIBLE)
            img5.setVisibility(INVISIBLE)
        }
        pink_color.setOnClickListener(){
            color = "Pink"
            card_add.setBackgroundResource(R.color.themecolor)
            img5.setVisibility(VISIBLE)
            img2.setVisibility(INVISIBLE)
            img3.setVisibility(INVISIBLE)
            img4.setVisibility(INVISIBLE)
            img1.setVisibility(INVISIBLE)
        }

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
            val color1 = intent.getStringExtra("color")
            title.setText(title_val)
            desc.setText(desc_val)
            img1.setVisibility(INVISIBLE)


            if (color1.toString() == "Blue"){
                color = "Blue"
                img1.setVisibility(VISIBLE)
                card_add.setBackgroundResource(R.color.blue)

            }else if(color1.toString() == "Yellow"){
                color = "Yellow"
                img2.setVisibility(VISIBLE)
                card_add.setBackgroundResource(R.color.yellow)

            }else if(color1.toString() == "Red"){
                color = "Red"
                img3.setVisibility(VISIBLE)
                card_add.setBackgroundResource(R.color.red)

            }else if(color1.toString() == "Black"){
                color = "Black"
                img4.setVisibility(VISIBLE)
                card_add.setBackgroundResource(R.color.black)

            }else if(color1.toString() == "Pink"){
                color = "Pink"
                img5.setVisibility(VISIBLE)
                card_add.setBackgroundResource(R.color.themecolor)

            }


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


            intent.putExtra("titleExtra", title.text.toString())
            intent.putExtra("messageExtra", desc.text.toString())

            if (hours_alarm > 0 && min_alarm > 0){
                scheduleNotification()
                println("yeeeee")
                todoDataViewModels.insertData(TodoData(id,title.text.toString(),desc.text.toString(),time.toString(),fulldate.toString(),color.toString(),hours_alarm.toString(),min_alarm.toString(),ampm_alarm,false))
                id = id+1
            }
            else{

                println("yeeeee")
                todoDataViewModels.insertData(TodoData(id,title.text.toString(),desc.text.toString(),time.toString(),fulldate.toString(),color.toString(),hours_alarm.toString(),min_alarm.toString(),ampm_alarm,false))
                id = id+1
                intent = Intent(applicationContext, TodoActivity::class.java)
                startActivity(intent)
            }
        }
        update_btn.setOnClickListener(){
            println("nooooooo")
            todoDataViewModels.insertData(TodoData(id1,
                title.text.toString(),
                desc.text.toString(),
                time.toString(),
                fulldate.toString(),
                color.toString(),hours_alarm.toString(),min_alarm.toString(),ampm_alarm,false))
            intent = Intent(applicationContext, TodoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showTimePicker() {

        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Appointment time")
                .build()


        //calender = Calendar.getInstance()
        picker.show(supportFragmentManager,"foxandroid")
        picker.addOnPositiveButtonClickListener{
            calender = Calendar.getInstance()
            calender[Calendar.HOUR_OF_DAY] = picker.hour
            calender[Calendar.MINUTE] = picker.minute
            calender[Calendar.SECOND] = 0
            calender[Calendar.MILLISECOND] = 0
            hours_alarm = picker.hour
            min_alarm = picker.minute
            time1.setText(picker.hour.toString() +" : " + picker.minute)
        }
    }


    private fun scheduleNotification() {

        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this,ReminderBrodcast::class.java)

        title1 = title.text.toString()
        des1 = desc.text.toString()
        intent.putExtra("title_Extra", title1)
        intent.putExtra("desc_Extra", des1)
        title1 = ""
        des1 = ""
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,calender.timeInMillis,
            AlarmManager.INTERVAL_DAY,pendingIntent
        )
        println("aaa")
        println(calender.timeInMillis)
        showAlert(title1,calender.time, des1)
    }

    private fun createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name: CharSequence = "Notif Channel"
            val desc = "A Description of the Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel= NotificationChannel("channel1",name,importance)
            channel.description = desc

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
//        val name = "Notif Channel"
//        val desc = "A Description of the Channel"
//        val importance = NotificationManager.IMPORTANCE_DEFAULT
//        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel(channelID, name, importance)
//        } else {
//            TODO("VERSION.SDK_INT < O")
//        }
//        channel.description = desc
//        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(channel)
    }

    private fun showAlert(title: String, time: Date, message: String) {

        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage("Title: "+ title + "Message" + message + "At: " + calender.time)
            .setPositiveButton("Okay"){_,_ ->
                intent = Intent(applicationContext, TodoActivity::class.java)
                startActivity(intent)
            }
            .show()
    }

    private fun getTime(): Long {
        val calendar = Calendar.getInstance()
        val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        var date =  current.dayOfMonth
        var month = current.monthValue
        var year = current.year
        calendar.set(year,month,date,hours_alarm,min_alarm)
        println("calendar.timeInMillis")
        println(calendar.timeInMillis)
        println(year)
        println(month)
        println(date)
        println(hours_alarm)
        println(min_alarm)
        return calendar.timeInMillis

    }

    override fun onBackPressed() {
        intent = Intent(applicationContext, TodoActivity::class.java)
        startActivity(intent)
    }

    private fun concat(hours: Int, minutes: Int): String? {
        return hours.toString() +":"+ minutes.toString()
    }
}