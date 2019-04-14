package com.furkanaskin.app.podpocket.di.module

import androidx.room.Room
import android.content.Context
import com.furkanaskin.app.podpocket.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun getDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context,
                AppDatabase::class.java, "example-db").build()
    }

}