package com.chen.peter.ptt_eater.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context


@Database(entities = arrayOf(Post::class),version = 1, exportSchema = false)
abstract class PostsDataBase: RoomDatabase() {
    companion object {
        private var instance: PostsDataBase? = null

        fun getInstance(context: Context): PostsDataBase {
            val builder = Room.databaseBuilder(context, PostsDataBase::class.java, "FoodPosts.db")
            instance = builder.allowMainThreadQueries().build()
            return instance!!
        }
    }
    abstract fun postsDao():PostDAO
}