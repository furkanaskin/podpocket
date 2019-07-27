package com.furkanaskin.app.podpocket.di.component

import android.app.Application
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        NetModule::class,
        DatabaseModule::class,
        ActivityModule::class,
        ViewModelModule::class
    ]
)

interface ApplicationComponent : AndroidInjector<Podpocket> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationBind(application: Application): Builder

        fun build(): ApplicationComponent
    }

    // fun inject(app: Podpocket)
}
