package com.furkanaskin.app.podpocket.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.furkanaskin.app.podpocket.db.dao.*
import com.furkanaskin.app.podpocket.db.entities.*

@Database(entities = [Example::class,
    UserEntity::class,
    PlayerEntity::class,
    EpisodeEntity::class,
    FavoriteEpisodeEntity::class,
    RecentlyPlaysEntity::class], version = 2)

abstract class AppDatabase : RoomDatabase() {
    abstract fun exampleDao(): ExampleDao
    abstract fun userDao(): UserDao
    abstract fun playerDao(): PlayerDao
    abstract fun episodesDao(): EpisodesDao
    abstract fun favoritesDao(): FavoritesDao
    abstract fun recentlyPlaysDao(): RecentlyPlaysDao
}