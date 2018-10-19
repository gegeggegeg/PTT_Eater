package com.chen.peter.ptt_eater.ui

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chen.peter.ptt_eater.R
import com.chen.peter.ptt_eater.database.Post



class PttFoodVIewHolder(view: View):RecyclerView.ViewHolder(view) {

    private val view:View
    private val title:TextView = view.findViewById(R.id.title)
    private val author:TextView = view.findViewById(R.id.author)
    private val address:TextView = view.findViewById(R.id.address)
    private val phonenumber:TextView = view.findViewById(R.id.phoneNumber)
    private val date:TextView = view.findViewById(R.id.date)
    private val picView:ImageView = view.findViewById(R.id.picview)
    private var post:Post? = null
    private var article = "test"

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
        Glide.with(itemView).load(post.imgsrc).into(picView)
        view.setOnClickListener{
                _->
            val intent = Intent(view.context,ArticleActivity::class.java)
            intent.putExtra("article",article)
            view.context.startActivity(intent)
        }

    }

}