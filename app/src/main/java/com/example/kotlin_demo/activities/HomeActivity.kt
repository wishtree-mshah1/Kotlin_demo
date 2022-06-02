package com.example.kotlin_demo.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_demo.R
import com.example.kotlin_demo.adapters.MyAdapter
import com.example.kotlin_demo.data.ApiDatabase
import com.example.kotlin_demo.data.MyDataItem
import com.example.kotlin_demo.data.UserDao
import com.example.kotlin_demo.interfaces.ApiInterface
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
    private lateinit var apiDataViewModels: ApiDataViewModels
    private lateinit var constrait:LinearLayout
    private lateinit var MyAdapter: MyAdapter
    private lateinit var manager:LinearLayoutManager
    var allData = ArrayList<MyDataItem>()
    var _start: Int = 0
    var _limit: Int = 10


    companion object {
        private const val STORAGE_PERMISSION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)
        welcomeMess = findViewById(R.id.textView9)
        auth= FirebaseAuth.getInstance()
        //NestedScrollView = findViewById(R.id.NestedScroll)
        //progressBar = findViewById(R.id.progressBar)
        constrait = findViewById<LinearLayout>(R.id.constrait)



        showDetails()

        var recyclerview_users = findViewById<RecyclerView>(R.id.recyclerview_users)
        recyclerview_users.layoutManager= LinearLayoutManager(this)
        val adapter = MyAdapter(this)
        recyclerview_users.adapter = adapter
        var view = layoutInflater.inflate(R.layout.row_progress, null)
        val layout = findViewById<LinearLayout>(R.id.layout)

        doNetworkCalls(_start, _limit, view, layout)

        val dao = ApiDatabase.getDatabase(applicationContext).getApiDataDao()
        val repository = APiDataRepository(dao)
        apiDataViewModels = ViewModelProvider(this,ApiDataVMFactory(repository)).get(ApiDataViewModels::class.java)
        apiDataViewModels.allData.observe(this, Observer {
            adapter.updateData(it)
        })



        recyclerview_users.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView!!.layoutManager?.itemCount
                println(totalItemCount)
                try {

                    if (!recyclerView.canScrollVertically(1) and (newState==RecyclerView.SCROLL_STATE_IDLE)) {
                        d("-----","end");
                        _start = _start+10
                        //progressBar.setVisibility(View.VISIBLE)
                        doNetworkCalls(_start,_limit,view,layout)

                    }
                }
                catch (e:Exception){
                    Log.e("Pagination Exception", e.toString())
                    //progressBar.setVisibility(View.VISIBLE)

                }


            }
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

    private fun doNetworkCalls(_start: Int, _limit: Int, view: View, layout: LinearLayout) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val responseDatas = async {

                    val retrofitBuilder = Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build()
                        .create(ApiInterface::class.java)

                    val retrofitData =  retrofitBuilder.getData(_start,_limit)


                    retrofitData.enqueue(object : Callback<List<MyDataItem>?> {
                        override fun onResponse(
                            call: Call<List<MyDataItem>?>,
                            response: Response<List<MyDataItem>?>,
                        ) {
                            println("rrrr")
                            println(response.body())
                            if (response.body().toString() == "[]"){
                                layout.removeView(view)
                            }
                            //!! for a not null
                            val responseBody = response.body()!!


                            for (i in 0..responseBody.size-1){

                                apiDataViewModels.insertData(MyDataItem(responseBody[i].body,responseBody[i].id,responseBody[i].title,responseBody[i].userId))
                                //progressBar.setVisibility(View.INVISIBLE)
                                layout.removeView(view)

                            }

                        }

                        override fun onFailure(call: Call<List<MyDataItem>?>, t: Throwable) {
                            d("HomeActivity","onFailure"+t.message)
                            //progressBar.setVisibility(View.INVISIBLE)
                            layout.removeView(view)

                        }
                    })
                }.await()
                if (responseDatas != null){
                    d("ABC","Called")
                }

                else{
                    layout.removeView(view)

                }
            }
        }
        catch (e:Exception){
            Log.e("Exception",e.localizedMessage)
            //progressBar.setVisibility(View.INVISIBLE)
            layout.removeView(view)

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