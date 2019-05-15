package com.furkanaskin.app.podpocket.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.furkanaskin.app.podpocket.db.entities.PlayerEntity

/**
 * Created by Furkan on 14.05.2019
 */

@Dao
interface PlayerDao {
    @Query("Select * from Player where id=0")
    fun getPlayer(): LiveData<PlayerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayer(player: PlayerEntity)

    @Query("Delete from Player where id>-1")
    fun deleteAll()

    @Update
    fun updatePlayer(player: PlayerEntity)

    @Delete
    fun deleteUser(player: PlayerEntity)
}