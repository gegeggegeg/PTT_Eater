package com.chen.peter.ptt_eater.network

import com.chen.peter.ptt_eater.database.Post
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class PostAdapter: Converter<ResponseBody,Post> {
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
        val main = document.select("main-content").text()
        val author = document.select("article-metaline").select("span").get(0)
    }
}