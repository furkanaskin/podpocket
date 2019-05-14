package com.furkanaskin.app.podpocket.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.furkanaskin.app.podpocket.db.dao.ExampleDao
import com.furkanaskin.app.podpocket.db.dao.PlayerDao
import com.furkanaskin.app.podpocket.db.dao.UserDao
import com.furkanaskin.app.podpocket.db.entities.Example
import com.furkanaskin.app.podpocket.db.entities.PlayerEntity
import com.furkanaskin.app.podpocket.db.entities.UserEntity

@Database(entities = [Example::class, UserEntity::class, PlayerEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exampleDao(): ExampleDao
    abstract fun userDao(): UserDao
    abstract fun playerDao(): PlayerDao
}