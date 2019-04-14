package com.furkanaskin.app.podpocket

import com.furkanaskin.app.podpocket.di.component.DaggerApplicationComponent
import com.furkanaskin.app.podpocket.di.module.ApplicationModule

class Podpocket : android.app.Application() {

    val component by lazy {
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }
}

