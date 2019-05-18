package com.furkanaskin.app.podpocket.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.furkanaskin.app.podpocket.db.entities.FavoriteEpisodeEntity

/**
 * Created by Furkan on 2019-05-18
 */

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM Favorites ORDER BY pubDateMs DESC")
    fun getFavoriteEpisodes(): LiveData<List<FavoriteEpisodeEntity>>

    @Query("SELECT * FROM Favorites WHERE id = :episodeId")
    fun getFavoriteEpisode(episodeId: String): FavoriteEpisodeEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteEpisode(episode: FavoriteEpisodeEntity?)

    @Update
    fun updateFavoriteEpisode(episode: FavoriteEpisodeEntity)

    @Query("DELETE FROM Favorites WHERE id = :episodeId")
    fun deleteFavoriteEpisode(episodeId: String)

    @Query("DELETE FROM Favorites")
    fun deleteAllFavoriteEpisodes()
}