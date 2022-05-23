package com.example.kotlin_demo.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_demo.R
import com.example.kotlin_demo.network.UserDao
import com.example.kotlin_demo.adapters.MyAdapter
import com.example.kotlin_demo.interfaces.ApiInterface
import com.example.kotlin_demo.models.MyDataItem
import com.example.kotlin_demo.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://jsonplaceholder.typicode.com/"

class HomeActivity : AppCompatActivity() {

    private  lateinit var welcomeMess: TextView
    private lateinit var auth: FirebaseAuth
    lateinit var myAdapter: MyAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    companion object {
        private const val STORAGE_PERMISSION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)

        welcomeMess = findViewById(R.id.textView9)
        auth= FirebaseAuth.getInstance()

        showDetails()

        var recycler = findViewById<RecyclerView>(R.id.recyclerview_users)
        recycler.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recycler.layoutManager = linearLayoutManager

        doNetworkCalls()

        
    }

    private fun showDetails() {
        UserDao().getUserbyId(auth.currentUser?.uid.toString()).addOnCompleteListener {
            val currentUser= it.result?.toObject(User::class.java)
            welcomeMess.text="Welcome ${currentUser?.name}"
        }
    }

    fun logout(view: android.view.View) {
        auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
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

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {
            Toast.makeText(this, "Already have permission of storage", Toast.LENGTH_SHORT).show()
        }
    }

    private fun doNetworkCalls(){
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val responseDatas = async {

                    val retrofitBuilder = Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build()
                        .create(ApiInterface::class.java)

                    val retrofitData =  retrofitBuilder.getData()

                    retrofitData.enqueue(object : Callback<List<MyDataItem>?> {
                        override fun onResponse(
                            call: Call<List<MyDataItem>?>,
                            response: Response<List<MyDataItem>?>
                        ) {
                            //!! for a not null
                            val responseBody = response.body()!!
                            myAdapter = MyAdapter(baseContext, responseBody)
                            myAdapter.notifyDataSetChanged()
                            var recycler = findViewById<RecyclerView>(R.id.recyclerview_users)
                            recycler.adapter = myAdapter

                        }

                        override fun onFailure(call: Call<List<MyDataItem>?>, t: Throwable) {
                            d("HomeActivity","onFailure"+t.message)
                        }
                    })
                }.await()
                if (responseDatas != null){
                    Log.d("ABC","Calledddddddddddddddddddd")
                }
            }
        }
        catch (e:Exception){
            Log.e("Exceptionnnnnnnnnn",e.localizedMessage)
        }

    }


    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
         if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}