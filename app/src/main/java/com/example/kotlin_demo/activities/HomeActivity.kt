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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_demo.R
import com.example.kotlin_demo.data.UserDao
import com.example.kotlin_demo.adapters.MyAdapter
import com.example.kotlin_demo.data.ApiDatabase
import com.example.kotlin_demo.interfaces.ApiInterface
import com.example.kotlin_demo.data.MyDataItem
import com.example.kotlin_demo.data.User
import com.example.kotlin_demo.repo.APiDataRepository
import com.example.kotlin_demo.viewmodels.ApiDataVMFactory
import com.example.kotlin_demo.viewmodels.ApiDataViewModels
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
//    lateinit var myAdapter: MyAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var apiDataViewModels: ApiDataViewModels
    var allData = ArrayList<MyDataItem>()

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

        var recyclerview_users = findViewById<RecyclerView>(R.id.recyclerview_users)
        recyclerview_users.layoutManager= LinearLayoutManager(this)
        val adapter = MyAdapter(this)
        recyclerview_users.adapter = adapter


      /*  var recycler = findViewById<RecyclerView>(R.id.recyclerview_users)
        recycler.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recycler.layoutManager = linearLayoutManager*/

        doNetworkCalls()
/*
        checkApiCalling()
*/

        val dao = ApiDatabase.getDatabase(applicationContext).getApiDataDao()
        val repository = APiDataRepository(dao)
        apiDataViewModels = ViewModelProvider(this,ApiDataVMFactory(repository)).get(ApiDataViewModels::class.java)
        apiDataViewModels.allData.observe(this, Observer {
            adapter.updateData(it)
        })
    }

    private fun showDetails() {
        try {
            UserDao().getUserbyId(auth.currentUser?.uid.toString()).addOnCompleteListener {
                //val currentUser= it.result?.toObject(User::class.java)
                //welcomeMess.text="Welcome ${currentUser?.name}"
            }
        }
        catch (e:Exception){
            println("!!!!!!!!!!!!!!!!!!!!!!!!")
            println(e)
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

    /*private fun checkApiCalling(){
        if(checkForInternet(baseContext) == false){
            if (allData.size == 0){

                doNetworkCalls()
            }
        }
        else{
            println("successfull")
        }
    }*/
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

                            for (i in 0..responseBody.size-1){
                                println(responseBody[i].body)
                                println(responseBody[i].id)
                                println(responseBody[i].title)
                                println(responseBody[i].userId)

                                apiDataViewModels.insertData(MyDataItem(responseBody[i].body,responseBody[i].id,responseBody[i].title,responseBody[i].userId))

                            }

                            println("////////////////////////////////////")
                            println(responseBody[1].body)
                            println(responseBody.size)
                            println("////////////////////////////////////")

                            /*myAdapter = MyAdapter(baseContext, responseBody)
                            myAdapter.notifyDataSetChanged()
                            var recycler = findViewById<RecyclerView>(R.id.recyclerview_users)
                            recycler.adapter = myAdapter*/

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