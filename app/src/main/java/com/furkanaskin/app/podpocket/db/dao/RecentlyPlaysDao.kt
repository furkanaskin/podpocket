package com.furkanaskin.app.podpocket.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.furkanaskin.app.podpocket.db.entities.RecentlyPlaysEntity

/**
 * Created by Furkan on 2019-07-03
 */

@Dao
interface RecentlyPlaysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecentlyPlay(recentlyPlaysEntity: RecentlyPlaysEntity)

    @Query("Delete From RecentlyPlays")
    fun deleteAll()

    @Query("Select * From RecentlyPlays Where type=:type")
    fun getRecentlyPlays(type: String): List<RecentlyPlaysEntity>

    @Query("Select * from RecentlyPlays where type='podcast' order by id asc")
    fun getRecentlyPlayedPodcasts(): List<RecentlyPlaysEntity>

    @Query("Select * from RecentlyPlays where type='episode' order by id asc")
    fun getRecentlyPlayedEpisodes(): List<RecentlyPlaysEntity>
}