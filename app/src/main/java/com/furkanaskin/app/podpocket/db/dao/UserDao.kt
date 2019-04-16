package com.furkanaskin.app.podpocket.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.furkanaskin.app.podpocket.db.entities.UserEntity

/**
 * Created by Furkan on 16.04.2019
 */

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserEntity)

    @Query("Delete from User where id>-1")
    fun deleteAll()

    @Query("Select * from User where id=:userID")
    fun getUser(userID: Int): LiveData<UserEntity>

    @Update
    fun updateUser(user: UserEntity)

    @Delete
    fun deleteUser(user: UserEntity)
}