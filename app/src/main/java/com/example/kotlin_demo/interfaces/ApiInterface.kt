package com.example.kotlin_demo.interfaces

import com.example.kotlin_demo.data.MyDataItem
import com.google.api.Page
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET(value = "posts?")
    fun getData(@Query("_start") _start: Int,
                @Query("_limit") _limit: Int): Call<List<MyDataItem>>

}