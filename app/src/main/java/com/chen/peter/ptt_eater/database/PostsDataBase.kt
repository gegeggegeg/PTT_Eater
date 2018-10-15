package com.chen.peter.ptt_eater.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context


@Database(entities = arrayOf(Post::class),version = 1, exportSchema = false)
abstract class PostsDataBase: RoomDatabase() {
    companion object {
        fun getInstance(context: Context): PostsDataBase {
            val builder = Room.databaseBuilder(context, PostsDataBase::class.java, "FoodPosts.db")
            return builder
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    abstract fun postsDao():PostDAO
}