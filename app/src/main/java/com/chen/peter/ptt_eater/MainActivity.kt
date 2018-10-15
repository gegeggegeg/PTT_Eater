package com.chen.peter.ptt_eater

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.TextView
import com.chen.peter.ptt_eater.database.PTTFoodRepo
import com.chen.peter.ptt_eater.database.Post
import com.chen.peter.ptt_eater.database.PostsDataBase
import com.chen.peter.ptt_eater.network.Page
import com.chen.peter.ptt_eater.network.PageAdapter
import com.chen.peter.ptt_eater.network.PostAdapter
import com.chen.peter.ptt_eater.network.PttAPI
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Whitelist
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val output = findViewById<TextView>(R.id.output)

        val getpage = Retrofit.Builder().
            baseUrl("https://www.ptt.cc/bbs/").
            addConverterFactory(PageAdapter.Factory).
            build().create(PttAPI::class.java)
        val getpost = Retrofit.Builder().
            baseUrl("https://www.ptt.cc/bbs/").
            addConverterFactory(PostAdapter.Factory).
            build().create(PttAPI::class.java)
        val db = PostsDataBase.getInstance(this)
        val repo = PTTFoodRepo(db,getpage,getpost)
        repo.refresh("Food/index.html")
    }
}
