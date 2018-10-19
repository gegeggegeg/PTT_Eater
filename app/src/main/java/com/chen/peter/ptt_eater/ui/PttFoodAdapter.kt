package com.chen.peter.ptt_eater.ui

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chen.peter.ptt_eater.R
import com.chen.peter.ptt_eater.database.Post

class PttFoodAdapter: PagedListAdapter<Post, PttFoodVIewHolder>(diffCallback) {

    companion object {
        val diffCallback = object :DiffUtil.ItemCallback<Post>(){
            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
               return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
               return oldItem.title == newItem.title
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PttFoodVIewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val holder = PttFoodVIewHolder(
            inflater.inflate(
                R.layout.food_view_holder,
                parent,
                false
            )
        )
        return holder
    }

    override fun onBindViewHolder(holder: PttFoodVIewHolder, position: Int) {
        try {
            holder.bind(getItem(position)!!)
        }catch (e:Exception){
            //do nothing
        }
    }






}