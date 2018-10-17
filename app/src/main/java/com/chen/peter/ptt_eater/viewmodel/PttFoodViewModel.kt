package com.chen.peter.ptt_eater.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import com.chen.peter.ptt_eater.database.PTTFoodRepo
import com.chen.peter.ptt_eater.database.Post

class PttFoodViewModel(private val repo : PTTFoodRepo): ViewModel() {



    fun refresh(){
        repo.refresh()
    }

    fun deleteAll(){
        repo.deleteAll()
    }

    fun isLoading():MutableLiveData<Boolean>{
        return repo.isLoading()
    }

    fun getLiveDataPagedlist():LiveData<PagedList<Post>>{
        return repo.getpagedList()
    }

}