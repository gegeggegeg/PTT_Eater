package com.chen.peter.ptt_eater.network

import android.util.Log
import com.chen.peter.ptt_eater.database.Post
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.util.regex.Pattern

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
        val post = Post("","","","","","","","")
        try  {
            val document = Jsoup.parse(response.string())
            document.outputSettings().prettyPrint(false)
            val text = document.getElementById("main-content").wholeText()
            val startindex = text.indexOf("餐廳")
            val endindex = text.indexOf("※ 發")
            var main:String =""
            try {
                main = text.substring(text.indexOf("2018")+4,endindex)
            }catch (e:Exception){
                main = text.substring(startindex, endindex)
            }
            try {
                post.article = main
                post.author = document.select("div.article-metaline").get(0).select("span.article-meta-value").text()
                post.title = document.select("div.article-metaline").get(1).select("span.article-meta-value").text()
                post.link = document.getElementById("main-content").select("a").attr("href")
                post.time = document.select("div.article-metaline").get(2).select("span.article-meta-value").text()
            }catch (e:Exception){
                Log.d(TAG,e.message+" rrrrr")
            }

            post.name = post.title.substring(post.title.indexOf("]")+1)

            try {
                post.address = text.substring(text.indexOf("址") + 2, text.indexOf("號")+1).trim()
            }catch (e:Exception){
                try {
                    post.address = text.substring(text.indexOf("址") + 2, text.indexOf("電")).trim()
                }catch (e:Exception){
                    post.address = ""
                    Log.d(TAG,e.message+" adress")
                }
            }
            try{
                var pnendindex = text.indexOf("電話：") + 3

                while (text[pnendindex].isDigit() || text[pnendindex].equals('(')
                    || text[pnendindex].equals(')')|| text[pnendindex].equals(' ')||text[pnendindex].equals('-'))
                    pnendindex++

                post.phoneNumber = text.substring(text.indexOf("電話：") + 3, pnendindex).trim()
                Log.d(TAG, post.phoneNumber)
            }catch (e:Exception){
                Log.d(TAG,"cant find phoneNumber"+e.message)
            }


        }catch (e:Exception){
            Log.d(TAG,e.message+" "+"here")
        }
        return post
    }

}