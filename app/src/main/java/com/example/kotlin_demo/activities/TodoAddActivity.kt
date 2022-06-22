package com.example.kotlin_demo.activities

import android.app.*
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.View
import android.view.View.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_demo.R
import com.example.kotlin_demo.adapters.CheckboxAdapter
import com.example.kotlin_demo.data.TaskDatabase
import com.example.kotlin_demo.data.Temp
import com.example.kotlin_demo.data.TodoData
import com.example.kotlin_demo.notification.ReminderBrodcast
import com.example.kotlin_demo.repo.TaskDataRepository
import com.example.kotlin_demo.repo.TodoDataRepository
import com.example.kotlin_demo.viewmodels.TaskDataVMFactory
import com.example.kotlin_demo.viewmodels.TaskViewModels
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
    var layout_id = 0
    var check_id = 1000
    var edt_id = 10000
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
    private lateinit var uploadImageTxt : TextView
    private lateinit var time1 : TextView
    private lateinit var layout_checkbox : LinearLayout
    private lateinit var layout_checkbox1 : LinearLayout
    private lateinit var title : TextInputEditText
    private lateinit var description : TextInputLayout
    private lateinit var desc : TextInputEditText
    private lateinit var todoDataViewModels: TodoDataViewModels
    private lateinit var taskViewModels: TaskViewModels
    private lateinit var blue_color: CircleImageView
    private lateinit var yellow_color: CircleImageView
    private lateinit var red_color: CircleImageView
    private lateinit var black_color: CircleImageView
    private lateinit var pink_color: CircleImageView
    //private lateinit var add_check: Button
    private lateinit var img1: ImageView
    private lateinit var img2: ImageView
    private lateinit var img3: ImageView
    private lateinit var img4: ImageView
    private lateinit var recyclerview_check: RecyclerView
    private lateinit var img5: ImageView
    private lateinit var radio_button_1: RadioButton
    private lateinit var radio_button_2: RadioButton
    //private lateinit var checkbox_layout: LinearLayout
    private lateinit var radioGroup: RadioGroup
    private lateinit var uploadImage: ImageView
    private lateinit var card_add: CardView
    private lateinit var alarmManager: AlarmManager
    private lateinit var layout : LinearLayout
    private lateinit var checkbox_check: CheckBox
    private lateinit var checkbox_edt: EditText
    private lateinit var edt_check: EditText
    private lateinit var adapter: CheckboxAdapter
    private lateinit var pendingIntent: PendingIntent
    private val pickImage = 100
    private var imageUri: Uri? = null
    var id: Long = getTime()
    var id1: Long = 0
    var id_check: Long = 0
    var color: String = "Blue"
    var tempp = 0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_add)
        createNotificationChannel()

        submit_btn =  findViewById(R.id.submit)
        update_btn =  findViewById(R.id.update)
        description = findViewById(R.id.description)
        uploadImageTxt =  findViewById(R.id.uploadImageTxt)
        //add_check =  findViewById(R.id.add_check)
        title = findViewById(R.id.title_txt)
        layout_checkbox = findViewById(R.id.layout_checkbox)
        //layout_checkbox1 = findViewById(R.id.layout_checkbox1)
        time1 = findViewById(R.id.time1)
        desc = findViewById(R.id.des_txt)
        uploadImage = findViewById(R.id.uploadImageView)
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
        radio_button_1 = findViewById(R.id.radio_button_1)
        radio_button_2 = findViewById(R.id.radio_button_2)
        radioGroup = findViewById(R.id.radioGroup)
        time_text = findViewById(R.id.timepick)
        var view = layoutInflater.inflate(R.layout.checkbox_task, null, false)
        layout = view.findViewById(R.id.layout_check)



        recyclerview_check = findViewById(R.id.recyclerview_check)
        recyclerview_check.layoutManager = LinearLayoutManager(this)
        adapter = CheckboxAdapter(this, this)
        recyclerview_check.adapter = adapter







        val dao = TaskDatabase.getDatabase(applicationContext).getTodoDataDao()
        val repository = TodoDataRepository(dao)
        todoDataViewModels = ViewModelProvider(this, TodoDataVMFactory(repository)).get(TodoDataViewModels::class.java)
        todoDataViewModels.allData.observe(this, Observer {
        })

        val dao1 = TaskDatabase.getDatabase(applicationContext).getTaskDataDao()
        val repository1 = TaskDataRepository(dao1)
        taskViewModels = ViewModelProvider(this,TaskDataVMFactory(repository1)).get(TaskViewModels::class.java)
        taskViewModels.allData1.observe(this, Observer {
        })

        val taskType = intent.getStringExtra("update_task")

        if(taskType.equals("update")){

        }
        else{
            todoDataViewModels.insertData(TodoData(id,"","","","","","","","","",false))
            taskViewModels.insertData(Temp(id,id_check,"",false))
        }

        layout.setOnClickListener(){
            println("okkkkkkk")
        }

        time_text.setOnClickListener {
            showTimePicker()
        }
        radio_button_1.setOnClickListener(){
            desc.setVisibility(VISIBLE)
            description.setVisibility(VISIBLE)
            layout_checkbox.setVisibility(GONE)

        }
        edt_check = view.findViewById(R.id.checkbox_edt)
        checkbox_check = view.findViewById(R.id.checkbox_check)

        radio_button_2.setOnClickListener(){

            taskViewModels.allData1.observe(this, Observer {
                adapter.updateTempData(it)
            })
            adapter.notifyDataSetChanged()

            tempp = tempp+1
            desc.setVisibility(GONE)
            description.setVisibility(GONE)
//            layout_checkbox1.setVisibility(VISIBLE)
            layout_checkbox.setVisibility(VISIBLE)
            //layout_checkbox.addView(view)
            recyclerview_check.setVisibility(VISIBLE)

        }



        uploadImage.setOnClickListener(){
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
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


        if (taskType.equals("update"))
        {
            update_btn.setVisibility(VISIBLE)
            submit_btn.setVisibility(INVISIBLE)
            val title_val = intent.getStringExtra("title")
            val image = intent.getStringExtra("image")
            val uri: Uri = Uri.parse(image)
            val desc_val =intent.getStringExtra("desc")
            id1 = intent.getLongExtra("id1",0)
            val color1 = intent.getStringExtra("color")
            title.setText(title_val)
            uploadImage.setImageURI(uri)
            imageUri = uri
            if (uri.toString() != "null"){
                uploadImageTxt.setVisibility(INVISIBLE)
            }
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

            println("edt_check.text")
            println(edt_check.text)
            println("edt_check.text")

            //println(checkbox_check.isChecked)
            intent.putExtra("titleExtra", title.text.toString())
            intent.putExtra("messageExtra", desc.text.toString())

            if (hours_alarm > 0 && min_alarm > 0){
                scheduleNotification()
                println("yeeeee")
                todoDataViewModels.insertData(TodoData(id,title.text.toString(),desc.text.toString(),current.toString(),fulldate.toString(),imageUri.toString(),color.toString(),hours_alarm.toString(),min_alarm.toString(),ampm_alarm,false))
//                id = id+1
            }
            else{
                println("yeeeee")
                todoDataViewModels.insertData(TodoData(id,title.text.toString(),desc.text.toString(),current.toString(),fulldate.toString(),imageUri.toString(),color.toString(),hours_alarm.toString(),min_alarm.toString(),ampm_alarm,false))
//                id = id+1
                intent = Intent(applicationContext, TodoActivity::class.java)
                startActivity(intent)
            }
        }
        update_btn.setOnClickListener(){
            println("nooooooo")
            todoDataViewModels.insertData(TodoData(id1,
                title.text.toString(),
                desc.text.toString(),
                current.toString(),
                fulldate.toString(),
                imageUri.toString(),
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            uploadImageTxt.setVisibility(View.INVISIBLE)
            imageUri = data?.data
            println("imageUri")
            println(imageUri)
            uploadImage.setImageURI(imageUri)
        }
    }

    override fun onBackPressed() {
        intent = Intent(applicationContext, TodoActivity::class.java)
        startActivity(intent)
    }

    private fun concat(hours: Int, minutes: Int): String? {
        return hours.toString() +":"+ minutes.toString()
    }

    fun setClick(title: Editable, check_status: Boolean) {

        var idd = id_check-1
        taskViewModels.insertData(Temp(id,idd,title.toString(),check_status))


    }
}