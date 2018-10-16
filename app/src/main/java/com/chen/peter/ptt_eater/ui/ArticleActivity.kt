package com.chen.peter.ptt_eater.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.chen.peter.ptt_eater.R

class ArticleActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.article_layout)
        var article_view = findViewById<TextView>(R.id.article_main)
        article_view.text = intent.getStringExtra("article")
    }
}