package com.chen.peter.ptt_eater.ui

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chen.peter.ptt_eater.R
import com.chen.peter.ptt_eater.database.Post
import com.chen.peter.ptt_eater.network.PttAPI
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.ref.WeakReference


class PttFoodVIewHolder(view: View,context: Context):RecyclerView.ViewHolder(view) {

    private val view:View
    private val title:TextView = view.findViewById(R.id.title)
    private val author:TextView = view.findViewById(R.id.author)
    private val address:TextView = view.findViewById(R.id.address)
    private val phonenumber:TextView = view.findViewById(R.id.phoneNumber)
    private val date:TextView = view.findViewById(R.id.date)
    private val imageView:ImageView =view.findViewById(R.id.thumbnail)
    private var post:Post? = null
    private var article = "test"
    private val context = context

    init {
        this.view = view
    }



    fun bind(post:Post){
        this.post = post
        title.text = post.title
        author.text = post.author
        address.text = post.address
        phonenumber.text = post.phoneNumber
        date.text = post.time
        article = post.article
        if(post.link.contains("pixnet")) {
            val fetchTask = FetchTask(PixnetUrl(post.link), imageView, view, article, context)
            fetchTask.execute()
        }else{
            view.setOnClickListener{
                    _->
                val intent = Intent(view.context,ArticleActivity::class.java)
                intent.putExtra("article",article)
                view.context.startActivity(intent)
            }
        }
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

    class FetchTask(pixnetUrl: PixnetUrl, imageView: ImageView,view: View, article:String, context: Context)
        : AsyncTask<Void, Void, ArrayList<String>>(){

        val pixnetUrl = WeakReference(pixnetUrl)
        val TAG = "FetchTask"
        var urlList = ArrayList<String>()
        val view = WeakReference(view)
        val article = WeakReference(article)
        val context = WeakReference(context)
        val imageView = WeakReference(imageView)

        override fun doInBackground(vararg params: Void?): ArrayList<String> {
            val pttAPI = Retrofit.Builder().baseUrl(pixnetUrl.get()!!.baseUrl).build().create(PttAPI::class.java)

            pttAPI.getPictureURLs(pixnetUrl.get()!!.subUrl).enqueue(
                object : Callback<ArrayList<String>> {
                    override fun onResponse(call: Call<ArrayList<String>>, response: Response<ArrayList<String>>) {
                        urlList = response.body()!!
                    }

                    override fun onFailure(call: Call<ArrayList<String>>, t: Throwable) {
                        Log.d(TAG,t.message)
                    }
                })
            return urlList
        }

        override fun onPostExecute(result: ArrayList<String>?) {

            Picasso.with(context.get()!!).load(result!!.get(1)).into(imageView.get()!!)
            view.get()!!.setOnClickListener{
                    _->
                val intent = Intent(view.get()!!.context,ArticleActivity::class.java)
                intent.putExtra("article",article.get()!!)
                view.get()!!.context.startActivity(intent)
            }

        }
    }
}