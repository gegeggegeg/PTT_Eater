package com.chen.peter.ptt_eater.network


import okhttp3.ResponseBody
import retrofit2.Call

import retrofit2.http.GET

interface PttAPI {
    @GET
    fun getCall(): Call<Page>

    @GET
    fun getSecondLier(): Call<ResponseBody>
}