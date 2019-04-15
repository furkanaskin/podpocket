package com.furkanaskin.app.podpocket.di.component

import android.content.Context
import android.content.SharedPreferences
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.di.module.ApplicationModule
import com.furkanaskin.app.podpocket.di.module.DatabaseModule
import com.furkanaskin.app.podpocket.di.module.NetModule
import com.furkanaskin.app.podpocket.ui.forget_password.ForgetPasswordViewModel
import com.furkanaskin.app.podpocket.ui.login.LoginViewModel
import com.furkanaskin.app.podpocket.ui.main.MainActivityViewModel
import com.furkanaskin.app.podpocket.ui.splash.SplashActivityViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton

@Component(modules = [ApplicationModule::class, NetModule::class, DatabaseModule::class])


interface ApplicationComponent {
    fun app(): Podpocket


    fun context(): Context

    fun preferences(): SharedPreferences

    fun inject(mainActivityViewModel: MainActivityViewModel)

    fun inject(splashActivityViewModel: SplashActivityViewModel)

    fun inject(loginViewModel: LoginViewModel)

    fun inject(forgetPasswordViewModel: ForgetPasswordViewModel)
}
