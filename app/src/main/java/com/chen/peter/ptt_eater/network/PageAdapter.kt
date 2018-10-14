package com.chen.peter.ptt_eater.network

import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Converter
import retrofit2.Retrofit
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
        val top:Elements = document.select("div.btn-group")
        val last:String = top.get(2).attr("href")
        val body:Elements = document.select("div.r-ent")
        val articleList:ArrayList<Page.Article> = ArrayList()
        for(element in body){
            val article:Page.Article = Page.Article(
                element.select("a").text(),
                element.select("a").attr("href"),
                element.select("div.author").text()
            )
            articleList.add(article)
        }
        val page = Page(last,articleList)
        return page
    }
}