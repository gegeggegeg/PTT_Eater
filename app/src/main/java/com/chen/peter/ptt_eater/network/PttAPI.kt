package com.chen.peter.ptt_eater.network


import com.chen.peter.ptt_eater.database.Post
import okhttp3.ResponseBody
import retrofit2.Call

import retrofit2.http.GET
import retrofit2.http.Path

interface PttAPI {
    @GET("{url}")
    fun getCall(@Path("url")url:String): Call<Page>

    @GET("{url}")
    fun getSecondLier(@Path("url")url:String): Call<Post>

    @GET("{url}")
    fun getPictureURLs (@Path("url")url:String): Call<ArrayList<String>>
}