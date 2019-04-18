package com.furkanaskin.app.podpocket

import android.app.Application
import com.facebook.stetho.Stetho
import com.furkanaskin.app.podpocket.di.component.DaggerApplicationComponent
import com.furkanaskin.app.podpocket.di.module.ApplicationModule

class Podpocket : Application() {

    val component by lazy {
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

    }
}

