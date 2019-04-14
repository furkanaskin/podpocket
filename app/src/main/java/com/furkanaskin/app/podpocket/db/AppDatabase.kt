package com.furkanaskin.app.podpocket.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.furkanaskin.app.podpocket.db.dao.ExampleDao
import com.furkanaskin.app.podpocket.db.entities.Example

@Database(entities = arrayOf(Example::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exampleDao(): ExampleDao
}