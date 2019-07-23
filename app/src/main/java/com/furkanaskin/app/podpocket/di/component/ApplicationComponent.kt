package com.furkanaskin.app.podpocket.di.component

import android.content.Context
import android.content.SharedPreferences
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.di.module.ApplicationModule
import com.furkanaskin.app.podpocket.di.module.DatabaseModule
import com.furkanaskin.app.podpocket.di.module.NetModule
import com.furkanaskin.app.podpocket.di.module.ViewModelModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
        modules = [
            ApplicationModule::class,
            NetModule::class,
            DatabaseModule::class,
            ViewModelModule::class
        ])

interface ApplicationComponent {

    fun app(): Podpocket

    fun context(): Context

    fun preferences(): SharedPreferences
}
