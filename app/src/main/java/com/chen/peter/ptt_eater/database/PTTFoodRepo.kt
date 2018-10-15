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
        private var list = ArrayList<Page.Article>()
    }

    val TAG = "PTTFoodRepo"

    @MainThread
    fun refresh(url:String){
        pttApi.getCall(url).enqueue(
            object :Callback<Page>{
                override fun onResponse(call: Call<Page>, response: Response<Page>) {
                    list.addAll(response.body()!!.articleList)
                    Log.d(TAG, list.size.toString())
                    if(list.size>pageSize){
                        for(element in list) {
                            Log.d(TAG, element.title + "\r\n")
                            val all = element.url
                            val sub = all.substring(all.indexOf("bbs/")+4)
                            loadPost(sub)
                        }
                    }else{
                        val last: String = response.body()!!.last
                        val next =  last.substring(last.indexOf("bbs/")+4)
                        Log.d(TAG,next)
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
                    Log.d(TAG,"Post asserted "+response.body()!!.title)
                    insertPostsIntoDb(response.body()!!)
                }
            }
        )
    }

    private  fun insertPostsIntoDb(Post: Post){
        database.postsDao().insert(Post)
    }



    private fun getpagedList(): LiveData<PagedList<Post>>{
        return LivePagedListBuilder(database.postsDao().requestPosts(),pageSize).build()
    }

}