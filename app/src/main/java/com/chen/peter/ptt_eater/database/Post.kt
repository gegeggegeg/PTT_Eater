package com.chen.peter.ptt_eater.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull


@Entity(tableName = "FoodPosts")
class Post (
             @PrimaryKey
             @NonNull
             val title:String,
             @NonNull
             val author:String,
             @NonNull
             val name: String,
             @NonNull
             val time: String,
             val article:String,
             val address:String,
             val phoneNumber:String,
             val link:String){
}