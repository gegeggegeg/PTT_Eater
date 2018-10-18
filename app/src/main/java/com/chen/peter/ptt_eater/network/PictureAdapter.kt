package com.chen.peter.ptt_eater.network

import android.util.Log
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.Exception
import java.lang.reflect.Type

class PictureAdapter : Converter<ResponseBody,ArrayList<String>> {

    val TAG = "PictureAdapter"
    object Factory:Converter.Factory(){
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
        ): Converter<ResponseBody, *>? {
            Log.d("Factory","Factory created")
            return PictureAdapter()
        }
    }

    override fun convert(responseBody: ResponseBody): ArrayList<String> {
        Log.d(TAG,"Start Converting")
        var list = ArrayList<String>()
        val document = Jsoup.parse(responseBody.string())
        try{
            val imageset = document.getElementsByTag("img")
            for(element in imageset){
                Log.d(TAG, element.absUrl("src"))
                if(element.absUrl("src").contains("pic.pimg.tw"))
                    list.add(element.absUrl("src"))
            }
            return  list
        }catch (e:Exception){
            Log.d(TAG,"Error: "+e.message)
        }
        return list
    }
}