package com.example.kotlin_demo.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.example.kotlin_demo.R
import com.example.kotlin_demo.network.UserDao
import com.example.kotlin_demo.models.User
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var name : TextInputEditText
    private lateinit var username : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var register : ImageButton
    private lateinit var signin : TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        val logincard = findViewById<CardView>(R.id.login_card)
        logincard.setBackgroundResource(R.drawable.cardback_login)

        name = findViewById(R.id.fullname)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        register = findViewById(R.id.Signup_button)
        signin = findViewById(R.id.sigin)
        auth = FirebaseAuth.getInstance()

        register.setOnClickListener {

            if (checkForInternet(this)) {
                val usernameinput = username.text.toString()
                val passinput =password.text.toString()
                registerUserInFirebase(usernameinput,passinput)

            }
            else {
                val builder = AlertDialog.Builder(this)
                //set title for alert dialog
                builder.setTitle("No Internet Connection")
                //set message for alert dialog
                builder.setMessage("Please connect Internet to move forward")
                builder.setIcon(R.drawable.ic_baseline_wifi_off_24)

                //performing positive action
                builder.setPositiveButton("Start Internet"){dialogInterface, which ->
                    val intent = Intent()
                    intent.component = ComponentName(
                        "com.android.settings",
                        "com.android.settings.Settings\$DataUsageSummaryActivity"
                    )
                    startActivity(intent)

                }
                //performing cancel action
                builder.setNeutralButton("Cancel"){dialogInterface , which ->
                    Toast.makeText(applicationContext,"clicked cancel",Toast.LENGTH_LONG).show()
                }

                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }



        }
        signin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun registerUserInFirebase(usernameinput: String, passinput: String) {
        auth.createUserWithEmailAndPassword(usernameinput,passinput).addOnCompleteListener {
            if (it.isSuccessful){
                val UserName = name.text.toString()
                appDataInFirestore(it.result?.user?.uid ,usernameinput, UserName)
                Toast.makeText(this@SignupActivity,"Register Successfully", Toast.LENGTH_LONG).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this@SignupActivity,"$usernameinput is already registered", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun appDataInFirestore(uid: String?, usernameinput: String, userName: String) {
        val user= User(uid!!,userName,usernameinput)
        UserDao().addUser(user)
    }


    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            // If android version below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


}