package com.furkanaskin.app.podpocket.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.furkanaskin.app.podpocket.db.entities.PostEntity

/**
 * Created by Furkan on 2019-07-05
 */

@Dao
interface PostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(postEntity: PostEntity?)

    @Query("Delete From Posts")
    fun deleteAll()

    @Query("Select * From Posts")
    fun getPosts(): LiveData<List<PostEntity>>

    @Query("Select * from Posts where region=:userRegion order by pubDate asc")
    fun getLocalePosts(userRegion: String): LiveData<List<PostEntity>>

}