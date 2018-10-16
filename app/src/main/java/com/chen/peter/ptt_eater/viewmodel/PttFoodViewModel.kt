package com.chen.peter.ptt_eater.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import com.chen.peter.ptt_eater.database.PTTFoodRepo
import com.chen.peter.ptt_eater.database.Post

class PttFoodViewModel(private val repo : PTTFoodRepo): ViewModel() {

    val subUrl:String = "Food/index.html"

    fun refresh(){
        repo.refresh(subUrl)
    }

    fun getLiveDataPagedlist():LiveData<PagedList<Post>>{
        return repo.getpagedList()
    }

}