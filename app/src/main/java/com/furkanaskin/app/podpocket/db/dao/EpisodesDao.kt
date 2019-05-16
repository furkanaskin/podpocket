package com.furkanaskin.app.podpocket.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.furkanaskin.app.podpocket.db.entities.EpisodeEntity

/**
 * Created by Furkan on 16.05.2019
 */

@Dao
interface EpisodesDao {

    @Query("SELECT * FROM Episode ORDER BY pubDateMs DESC")
    fun getEpisodes(): LiveData<List<EpisodeEntity>>

    @Query("SELECT * FROM Episode WHERE id = :episodeId")
    fun getEpisode(episodeId: String): EpisodeEntity

    @Query("SELECT * FROM Episode WHERE isSelected = 1")
    fun getPlayingEpisode(): List<EpisodeEntity>

    @Insert(onConflict = REPLACE)
    fun insertEpisode(episode: EpisodeEntity?)

    @Update
    fun updateEpisode(episode: EpisodeEntity)

    @Query("DELETE FROM Episode")
    fun deleteAllEpisodes()
}