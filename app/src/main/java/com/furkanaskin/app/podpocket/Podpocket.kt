package com.furkanaskin.app.podpocket

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.core.CrashlyticsCore
import com.facebook.stetho.Stetho
import com.furkanaskin.app.podpocket.di.component.DaggerApplicationComponent
import com.furkanaskin.app.podpocket.di.module.ApplicationModule
import com.jakewharton.threetenabp.AndroidThreeTen
import io.fabric.sdk.android.Fabric

class Podpocket : Application() {

    val component by lazy {
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        initFabric()
        Stetho.initializeWithDefaults(this)
    }


    private fun initFabric() {

        val core = CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build()

        Fabric.with(this, Crashlytics.Builder().core(core).build(), Answers())
    }
}

