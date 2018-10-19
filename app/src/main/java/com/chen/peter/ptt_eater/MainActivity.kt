package com.chen.peter.ptt_eater

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import com.chen.peter.ptt_eater.database.PTTFoodRepo
import com.chen.peter.ptt_eater.database.PostsDataBase
import com.chen.peter.ptt_eater.network.PageAdapter
import com.chen.peter.ptt_eater.network.PostAdapter
import com.chen.peter.ptt_eater.network.PttAPI
import com.chen.peter.ptt_eater.ui.PttFoodAdapter
import com.chen.peter.ptt_eater.viewmodel.PttFoodViewModel
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    private lateinit var model: PttFoodViewModel
    private lateinit var progressBar:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model = getViewModel()
        progressBar = findViewById(R.id.isloadingbar)
        initRecyclerView()
    }

    private fun getViewModel(): PttFoodViewModel{
        return ViewModelProviders.of(this,object :ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val getpage = Retrofit.Builder().
                    baseUrl("https://www.ptt.cc/bbs/").
                    addConverterFactory(PageAdapter.Factory).
                    build().create(PttAPI::class.java)
                val getpost = Retrofit.Builder().
                    baseUrl("https://www.ptt.cc/bbs/").
                    addConverterFactory(PostAdapter.Factory).
                    build().create(PttAPI::class.java)
                val db = PostsDataBase.getInstance(this@MainActivity)
                db.clearAllTables()
                val repo = PTTFoodRepo(db,getpage,getpost)

                @Suppress("UNCHECKED_CAST")
                return PttFoodViewModel(repo) as T
            }
        })[PttFoodViewModel::class.java]
    }

    private fun initRecyclerView(){

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView1)
        val mlayoutmanager = LinearLayoutManager(this)
        recyclerview.layoutManager = mlayoutmanager
        val adapter = PttFoodAdapter()
        recyclerview.adapter = adapter

        model.getLiveDataPagedlist().observe(this, Observer {
                posts-> if(posts != null)
                    adapter.submitList(posts)})

        model.isLoading().observe(this, Observer {
            isloading->
            if(isloading!!){
                recyclerview.isLayoutFrozen = true
                progressBar.visibility = View.VISIBLE
            }else{
                val handler = Handler()
                handler.postDelayed(object :Runnable{
                    override fun run() {
                        recyclerview.isLayoutFrozen = false
                        progressBar.visibility = View.INVISIBLE
                    }
                },1000)
            }
        })
    }
}
