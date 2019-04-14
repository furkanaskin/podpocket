package com.furkanaskin.app.podpocket.di.module

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.furkanaskin.app.podpocket.Podpocket
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(var app: Podpocket) {


    @Provides
    @Singleton
    fun provideApp(): Podpocket = app

    @Provides
    @Singleton
    fun provideContext(): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(app)
}
