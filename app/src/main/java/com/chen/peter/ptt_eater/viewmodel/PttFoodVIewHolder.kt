package com.chen.peter.ptt_eater.viewmodel

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.chen.peter.ptt_eater.R
import com.chen.peter.ptt_eater.database.Post


class PttFoodVIewHolder(view: View):RecyclerView.ViewHolder(view) {
    private val title:TextView = view.findViewById(R.id.title)
    private val author:TextView = view.findViewById(R.id.author)
    private val address:TextView = view.findViewById(R.id.address)
    private val phonenumber:TextView = view.findViewById(R.id.phoneNumber)
    private val link:TextView = view.findViewById(R.id.link)
    private val date:TextView = view.findViewById(R.id.date)
    private var post:Post? = null

    fun bind(post:Post){
        this.post = post
        title.text = post.title
        author.text = post.author
        address.text = post.address
        phonenumber.text = post.phoneNumber
        link.text = post.link
        date.text = post.time
    }

}