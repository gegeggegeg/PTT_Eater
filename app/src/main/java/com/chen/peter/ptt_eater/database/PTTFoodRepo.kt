package com.chen.peter.ptt_eater.database

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.os.AsyncTask
import android.support.annotation.MainThread
import android.util.Log
import com.chen.peter.ptt_eater.network.Page
import com.chen.peter.ptt_eater.network.PostAdapter
import com.chen.peter.ptt_eater.network.PttAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class PTTFoodRepo( val database:PostsDataBase,
                   private val pttApi: PttAPI,
                   private val getPostApi: PttAPI,
                   private  val pageSize:Int = 10){
    companion object {
        private var counter = 0
    }

    val TAG = "PTTFoodRepo"

    @MainThread
    fun refresh(url:String){
        pttApi.getCall(url).enqueue(
            object :Callback<Page>{
                override fun onResponse(call: Call<Page>, response: Response<Page>) {

                    counter +=  response.body()!!.articleList.size

                    for(element in response.body()!!.articleList) {
                        val all = element.url
                        val sub = all.substring(all.indexOf("bbs/")+4)
                        loadPost(sub)
                    }

                    if(counter>pageSize){
                        counter = 0
                    }else{
                        val last: String = response.body()!!.last
                        val next =  last.substring(last.indexOf("bbs/")+4)
                        refresh(next)
                    }
                }
                override fun onFailure(call: Call<Page>, t: Throwable) {
                    Log.d(TAG,"Failed to retrieve data")
                }
            }
        )
    }

    @MainThread
    private fun loadPost(link : String){
        getPostApi.getSecondLier(link).enqueue(
            object :Callback<Post>{
                override fun onFailure(call: Call<Post>, t: Throwable) {
                    Log.d(TAG,"Failed to load Post "+t.message)
                }
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if(response.body()!!.title == "" ) {
                        Log.d(TAG,"Abandon Post" + response.body()!!.title)
                    }else {
                        Log.d(TAG, "Post asserted " + response.body()!!.title)
                        insertPostsIntoDb(response.body()!!)
                    }
                }
            }
        )
    }

    public  fun insertPostsIntoDb(Post: Post){
        database.postsDao().insert(Post)
    }



    public fun getpagedList(): LiveData<PagedList<Post>>{
        return LivePagedListBuilder(database.postsDao().requestPosts(),pageSize).build()
    }

}