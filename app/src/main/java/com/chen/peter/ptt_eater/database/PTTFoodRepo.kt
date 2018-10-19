package com.chen.peter.ptt_eater.database

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.os.AsyncTask
import android.support.annotation.MainThread
import android.util.Log
import com.chen.peter.ptt_eater.network.Page
import com.chen.peter.ptt_eater.network.PictureAdapter
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

    }

    private val loading:MutableLiveData<Boolean> = MutableLiveData()
    private val TAG = "PTTFoodRepo"
    private val FrontPage:String = "Food/index.html"

    init {
        loading.value = false
    }

    @MainThread
    fun refresh(url:String = next ){
        loading.postValue(true)
        Log.d(TAG, loading.value.toString())
        pttApi.getCall(url).enqueue(
            object :Callback<Page>{
                override fun onResponse(call: Call<Page>, response: Response<Page>) {
                    //loading.postValue(false)
                    Log.d(TAG, loading.value.toString())
                    val last: String = response.body()!!.last
                    next =  last.substring(last.indexOf("bbs/")+4)
                    Log.d(TAG,"next = "+ next)
                    counter +=  response.body()!!.articleList.size

                    for(element in response.body()!!.articleList.asReversed()) {
                        val all = element.url
                        val sub = all.substring(all.indexOf("bbs/")+4)
                        loadPost(sub)
                    }
                }

                override fun onFailure(call: Call<Page>, t: Throwable) {
                    loading.postValue(false)
                    Log.d(TAG,"Failed to retrieve data "+t.message)
                }
            }
        )
    }

    @MainThread
    private fun loadPost(link : String){
        //loading.postValue(true)
        getPostApi.getSecondLier(link).enqueue(
            object :Callback<Post>{
                override fun onFailure(call: Call<Post>, t: Throwable) {
                    loading.postValue(false)
                    Log.d(TAG,"Failed to load Post "+t.message)
                }
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    //loading.postValue(false)
                    if(response.body()!!.title == "" ) {
                        Log.d(TAG,"Abandon Post" + response.body()!!.title)
                    }else {
                      val post = response.body()!!
                        loadPicUrls(post)
                    }
                }
            }
        )
    }

    private fun loadPicUrls(post: Post) {
        if(post.link.contains("pixnet")) {
            //loading.postValue(true)
            val pixnetUrl = PixnetUrl(post.link)
            try{
                val picAPI = Retrofit.Builder().addConverterFactory(PictureAdapter.Factory)
                .baseUrl(pixnetUrl.baseUrl).build().create(PttAPI::class.java)
                picAPI.getPictureURLs(pixnetUrl.subUrl).enqueue(
                object : Callback<ArrayList<String>> {
                    override fun onResponse(call: Call<ArrayList<String>>, response: Response<ArrayList<String>>) {
                        try {
                            var index = 0
                            post.imgsrc = response.body()!![index]

                            while(post.imgsrc.contains("gif")){
                                index += 1
                                post.imgsrc = response.body()!![index]
                            }

                            Log.d(TAG,post.imgsrc)
                        } catch (e: Exception) {
                            Log.d(TAG, e.message)
                        }
                        if (post.address.length < 40 &&
                            post.phoneNumber.length < 40 &&
                            post.author.length < 40 &&
                            post.title.length < 40
                        )
                            insertPostsIntoDb(post)
                        loading.postValue(false)
                    }
                    override fun onFailure(call: Call<ArrayList<String>>, t: Throwable) {
                        loading.postValue(false)
                        Log.d(TAG, t.message)
                    }
                })
            }catch (e:Exception){
                Log.d(TAG,e.message)
            }
        }else{
            if (post.address.length < 40 &&
                post.phoneNumber.length < 40 &&
                post.author.length < 40 &&
                post.title.length < 40
            )
                insertPostsIntoDb(post)
                loading.postValue(false)
        }
    }

    fun insertPostsIntoDb(Post: Post){
        database.postsDao().insert(Post)
    }

    fun isLoading():MutableLiveData<Boolean>{
        return loading
    }



    fun getpagedList(): LiveData<PagedList<Post>>{
        val config = PagedList.Config.Builder().
            setPageSize(30).
            setInitialLoadSizeHint(30).
            setPrefetchDistance(20).
            setEnablePlaceholders(false)
            .build()
        return LivePagedListBuilder(database.postsDao().requestPosts(),config)
            .setBoundaryCallback(object :PagedList.BoundaryCallback<Post>(){
                override fun onZeroItemsLoaded() {
                    if(!loading.value!!)
                        refresh(FrontPage)
                }

                override fun onItemAtEndLoaded(itemAtEnd: Post) {
                    if(!loading.value!!)
                        refresh(next)
                }
            })
            .build()
    }

    fun deleteAll(){
        database.postsDao().deleteAll()
    }

    class PixnetUrl(rawUrl: String){
        val baseUrl:String
        val subUrl:String
        init {
            val index = rawUrl.indexOf("post/")+5
            baseUrl = rawUrl.substring(0,index).trim()
            subUrl = rawUrl.substring(index).trim()
        }
    }



}