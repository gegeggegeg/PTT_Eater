package com.chen.peter.ptt_eater.network

import android.util.Log
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.Exception
import java.lang.reflect.Type

class PageAdapter:Converter<ResponseBody,Page>{
    object Factory:Converter.Factory(){
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
        ): Converter<ResponseBody, *>? {
            val adapter:PageAdapter = PageAdapter()
            if(type == Page::class.java)
                return adapter
            return null
        }
    }
    override fun convert(responsebody: ResponseBody): Page {
        val document:Document = Jsoup.parse(responsebody.string())
        val top= document.select("div.btn-group.btn-group-paging")
        val last = top.select("a").get(1).attr("href")
        Log.d("TEST1",last)
        val body:Elements = document.select("div.r-ent")
        val articleList:ArrayList<Page.Article> = ArrayList()
        for(element in body){
            try{
                if(element.select("a").get(0).text().contains("食記")||
                    element.select("a").get(0).text().contains("廣宣")) {
                    Log.d("test5",element.select("a").get(0).text())
                    Log.d("test5",element.select("a").attr("href"))
                    Log.d("test5",element.select("div.author").text())
                    val article: Page.Article = Page.Article(
                        element.select("a").get(0).text(),
                        element.select("a").attr("href"),
                        element.select("div.author").text()
                    )
                    articleList.add(article)
                    }
                }catch (e:Exception){
                    Log.d("error123",e.message)
                }
            }
        val page = Page(last,articleList)
        return page
    }
}