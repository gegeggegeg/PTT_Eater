package com.chen.peter.ptt_eater.network


class Page(val last:String,
           val articleList:ArrayList<Article>){
    class Article(val title:String,
                  val url:String,
                  val author:String){

    }
}