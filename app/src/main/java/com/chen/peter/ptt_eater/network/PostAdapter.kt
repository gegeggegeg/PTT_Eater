package com.chen.peter.ptt_eater.network

import android.util.Log
import com.chen.peter.ptt_eater.database.Post
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class PostAdapter: Converter<ResponseBody,Post> {

    val TAG = "PostAdapter"
    object Factory:Converter.Factory(){
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
        ): Converter<ResponseBody, *>? {
            val adapter = PostAdapter()
            if(type == Post::class.java)
                return adapter
            return null
        }
    }
    override fun convert(response: ResponseBody): Post {
        val document = Jsoup.parse(response.string())
        document.outputSettings().prettyPrint(false)
        val text = document.getElementById("main-content").wholeText()
        val startindex = text.indexOf("餐廳")
        val endindex  = text.indexOf("※ 發")
        val main = text.substring(startindex,endindex)
        val author = document.select("div.article-metaline").get(0).select("span.article-meta-value").text()
        val title = document.select("div.article-metaline").get(1).select("span.article-meta-value").text()
        val name = text.substring(text.indexOf("餐廳名稱：")+5,text.indexOf(" 消")).trim()
        val date = document.select("div.article-metaline").get(2).select("span.article-meta-value").text()
        val address = text.substring(text.indexOf("地址：")+3, text.indexOf("電")).trim()
        val pn = text.substring(text.indexOf("電話：")+3, text.indexOf("營")).trim()
        val link = document.getElementById("main-content").select("a").attr("href")
        val post = Post(title,author,name,date,main,address,pn,link)
        return post
    }
}