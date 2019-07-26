package com.furkanaskin.app.podpocket.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.furkanaskin.app.podpocket.di.ViewModelFactory
import com.furkanaskin.app.podpocket.di.key.ViewModelKey
import com.furkanaskin.app.podpocket.ui.after_register.AfterRegisterViewModel
import com.furkanaskin.app.podpocket.ui.dashboard.DashboardViewModel
import com.furkanaskin.app.podpocket.ui.feed.FeedViewModel
import com.furkanaskin.app.podpocket.ui.feed.feed_search.FeedSearchViewModel
import com.furkanaskin.app.podpocket.ui.feed.global_feed.GlobalFeedViewModel
import com.furkanaskin.app.podpocket.ui.feed.locale_feed.LocaleFeedViewModel
import com.furkanaskin.app.podpocket.ui.feed.new_post.NewPostViewModel
import com.furkanaskin.app.podpocket.ui.feed.post_detail.PostDetailViewModel
import com.furkanaskin.app.podpocket.ui.forget_password.ForgetPasswordViewModel
import com.furkanaskin.app.podpocket.ui.home.HomeViewModel
import com.furkanaskin.app.podpocket.ui.login.LoginViewModel
import com.furkanaskin.app.podpocket.ui.main.MainActivityViewModel
import com.furkanaskin.app.podpocket.ui.player.PlayerQueueViewModel
import com.furkanaskin.app.podpocket.ui.player.PlayerViewModel
import com.furkanaskin.app.podpocket.ui.podcast.PodcastViewModel
import com.furkanaskin.app.podpocket.ui.podcast.episodes.EpisodesViewModel
import com.furkanaskin.app.podpocket.ui.podcast.podcast_detail.PodcastDetailViewModel
import com.furkanaskin.app.podpocket.ui.profile.ProfileViewModel
import com.furkanaskin.app.podpocket.ui.profile.account_detail.AccountDetailViewModel
import com.furkanaskin.app.podpocket.ui.profile.favorites.FavoritesViewModel
import com.furkanaskin.app.podpocket.ui.profile.recently_played.RecentlyPlayedViewModel
import com.furkanaskin.app.podpocket.ui.search.SearchViewModel
import com.furkanaskin.app.podpocket.ui.settings.SettingsViewModel
import com.furkanaskin.app.podpocket.ui.splash.SplashActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Furkan on 2019-07-23
 */

@Module
abstract class ViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(SplashActivityViewModel::class)
    abstract fun provideSplashViewModel(splashActivityViewModel: SplashActivityViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(LoginViewModel::class)
    abstract fun provideLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun provideMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel


    //TODO: Add viewmodels which need to Dagger
    //TODO: You need to extend BaseActivity like below
    // @Inject constructor(api: PodpocketAPI, appDatabase: AppDatabase) : BaseViewModel(api, appDatabase)

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}