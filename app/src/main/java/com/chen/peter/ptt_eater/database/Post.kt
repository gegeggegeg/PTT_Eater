package com.chen.peter.ptt_eater.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull


@Entity(tableName = "FoodPosts")
class Post (
             @PrimaryKey
             @NonNull
             var title:String,
             @NonNull
             var author:String,
             @NonNull
             var name: String,
             @NonNull
             var time: String,
             var article:String,
             var address:String,
             var phoneNumber:String,
             var link:String,
             var imgsrc:String){
}