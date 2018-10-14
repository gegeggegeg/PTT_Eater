package com.chen.peter.ptt_eater.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context


@Database(entities = arrayOf(Post::class),version = 1, exportSchema = false)
abstract class PostsDataBase: RoomDatabase() {

    fun getInstance(context: Context):PostsDataBase{
        val builder = Room.databaseBuilder(context,PostsDataBase::class.java,"ptt.db")
        return builder
            .fallbackToDestructiveMigration()
            .build()
    }

    abstract fun postsDao():PostDAO
}