package com.furkanaskin.app.podpocket.di.module

import com.furkanaskin.app.podpocket.di.scope.ActivityScope
import com.furkanaskin.app.podpocket.ui.after_register.AfterRegisterActivity
import com.furkanaskin.app.podpocket.ui.dashboard.DashboardActivity
import com.furkanaskin.app.podpocket.ui.forget_password.ForgetPasswordActivity
import com.furkanaskin.app.podpocket.ui.login.LoginActivity
import com.furkanaskin.app.podpocket.ui.main.MainActivity
import com.furkanaskin.app.podpocket.ui.player.PlayerActivity
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
    internal abstract fun dasboardActivity(): DashboardActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun mainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun loginActivity(): LoginActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun playerActivity(): PlayerActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun forgetPasswordActivity(): ForgetPasswordActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun afterRegisterActivity(): AfterRegisterActivity
}