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
        private var next = ""
        private var loading = false
    }

    val TAG = "PTTFoodRepo"

    @MainThread
    fun refresh(url:String ){
        loading  = true
        pttApi.getCall(url).enqueue(
            object :Callback<Page>{
                override fun onResponse(call: Call<Page>, response: Response<Page>) {
                    loading = false
                    val last: String = response.body()!!.last
                    next =  last.substring(last.indexOf("bbs/")+4)
                    Log.d("test3", next)
                    counter +=  response.body()!!.articleList.size

                    for(element in response.body()!!.articleList) {
                        val all = element.url
                        val sub = all.substring(all.indexOf("bbs/")+4)
                        loadPost(sub)
                    }
                }
                override fun onFailure(call: Call<Page>, t: Throwable) {
                    loading = false
                    Log.d("test7","Failed to retrieve data "+t.message)
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
                      val post = response.body()!!
                        if(post.address.length<30 &&
                            post.phoneNumber.length<30&&
                            post.author.length<30&&
                            post.title.length<30)
                            insertPostsIntoDb(response.body()!!)
                    }
                }
            }
        )
    }

    fun insertPostsIntoDb(Post: Post){
        database.postsDao().insert(Post)
    }



    fun getpagedList(): LiveData<PagedList<Post>>{
        val config = PagedList.Config.Builder().
            setPageSize(15).
            setInitialLoadSizeHint(15).
            setPrefetchDistance(5).
            setEnablePlaceholders(false)
            .build()
        return LivePagedListBuilder(database.postsDao().requestPosts(),config)
            .setBoundaryCallback(object :PagedList.BoundaryCallback<Post>(){
                override fun onItemAtEndLoaded(itemAtEnd: Post) {
                    if(!loading)
                        refresh(next)
                }
            })
            .build()
    }




}