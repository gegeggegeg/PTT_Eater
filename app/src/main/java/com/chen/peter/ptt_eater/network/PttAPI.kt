package com.chen.peter.ptt_eater.network


import com.chen.peter.ptt_eater.database.Post
import okhttp3.ResponseBody
import retrofit2.Call

import retrofit2.http.GET

interface PttAPI {
    @GET("Food/index.html")
    fun getCall(): Call<Page>

    @GET("Food/M.1539521174.A.95A.html")
    fun getSecondLier(): Call<Post>
}