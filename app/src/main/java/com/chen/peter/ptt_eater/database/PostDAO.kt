package com.chen.peter.ptt_eater.database

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query


@Dao
interface PostDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: List<Post>)

    @Query("SELECT * FROM FoodPosts")
    fun requestPosts() : DataSource.Factory<Int,Post>

}