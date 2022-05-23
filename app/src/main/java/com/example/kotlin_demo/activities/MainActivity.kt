package com.example.kotlin_demo.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.kotlin_demo.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var email : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var login : ImageButton
    private lateinit var signup : TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.toolbarcolor)

        val logincard = findViewById<CardView>(R.id.login_card)
        logincard.setBackgroundResource(R.drawable.cardback_login)

        email = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.Signin_button)
        signup = findViewById(R.id.signup)
        auth = FirebaseAuth.getInstance()


        login.setOnClickListener {

            if (checkForInternet(this)) {
                val userinput = email.text.toString()
                val passinput =password.text.toString()

                checkcredential(userinput, passinput)
            } else {
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
        signup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

    }

    private fun checkcredential(userinput: String, passinput: String) {
        if (userinput.isNotEmpty() && passinput.isNotEmpty()){
            auth.signInWithEmailAndPassword(userinput,passinput).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this,"Logged in", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                else{
                    Toast.makeText(this,"Wrong credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }
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