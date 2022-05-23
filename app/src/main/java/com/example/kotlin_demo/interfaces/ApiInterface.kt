package com.example.kotlin_demo.interfaces

import com.example.kotlin_demo.models.MyDataItem
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {



    @GET(value = "posts")
    fun getData(): Call<List<MyDataItem>>
}