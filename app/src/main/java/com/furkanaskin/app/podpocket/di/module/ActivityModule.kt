package com.furkanaskin.app.podpocket.di.module

import com.furkanaskin.app.podpocket.di.scope.ActivityScope
import com.furkanaskin.app.podpocket.ui.login.LoginActivity
import com.furkanaskin.app.podpocket.ui.main.MainActivity
import com.furkanaskin.app.podpocket.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun splashActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun mainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun loginActivity(): LoginActivity

    //TODO : Add activities which need to use Dagger

}