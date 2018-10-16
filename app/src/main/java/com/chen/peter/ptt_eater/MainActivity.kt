package com.chen.peter.ptt_eater

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.AbsListView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model = getViewModel()
        initRecyclerView()
        model.refresh()
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
//        recyclerview.addOnScrollListener(object :RecyclerView.OnScrollListener(){
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                val visibleItemCount = mlayoutmanager.childCount
//                val totalitemCount = mlayoutmanager.itemCount
//                val pastVisibleItems = mlayoutmanager.findFirstVisibleItemPosition()
//                if(pastVisibleItems+visibleItemCount >= totalitemCount){
//                    model.refresh()
//                }
//            }
//        })
        model.getLiveDataPagedlist().observe(this, Observer { posts->
            if(posts != null) adapter.submitList(posts)})
    }
}
