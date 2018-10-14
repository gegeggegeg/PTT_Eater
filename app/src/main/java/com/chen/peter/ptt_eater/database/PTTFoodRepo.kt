package com.chen.peter.ptt_eater.database

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.support.annotation.MainThread
import com.chen.peter.ptt_eater.network.Page
import com.chen.peter.ptt_eater.network.PttAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class PTTFoodRepo( val database:PostsDataBase,
                   private var pttApi: PttAPI,
                   private  val pageSize:Int = 10){
    @MainThread
    private fun refresh(){
        pttApi.getCall().enqueue(
            object :Callback<Page>{
                override fun onResponse(call: Call<Page>, response: Response<Page>) {

                }
                override fun onFailure(call: Call<Page>, t: Throwable) {

                }
            }
        )
    }

    @MainThread
    private fun loadPost(urls:ArrayList<String>){
        val builder = Retrofit.Builder()
        for (url in urls) {
            val retrofit = builder.baseUrl(url).build()
            pttApi = retrofit.create(PttAPI::class.java)
            pttApi.getSecondLier().enqueue()
        }
    }

    private  fun insertPostsIntoDb(Posts: List<Post>){
        database.postsDao().insert(Posts)
    }



    private fun getpagedList(): LiveData<PagedList<Post>>{
        return LivePagedListBuilder(database.postsDao().requestPosts(),pageSize).build()
    }

}