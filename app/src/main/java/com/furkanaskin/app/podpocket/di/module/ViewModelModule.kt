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

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @IntoMap
    @Binds
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun provideMainViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel

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
    @ViewModelKey(ForgetPasswordViewModel::class)
    abstract fun provideForgetPasswordViewModel(forgetPasswordViewModel: ForgetPasswordViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(DashboardViewModel::class)
    abstract fun provideDashboardViewModel(dashboardViewModel: DashboardViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(HomeViewModel::class)
    abstract fun provideHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(SearchViewModel::class)
    abstract fun provideSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(FeedViewModel::class)
    abstract fun provideFeedViewModel(feedViewModel: FeedViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(ProfileViewModel::class)
    abstract fun provideProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(SettingsViewModel::class)
    abstract fun provideSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(AfterRegisterViewModel::class)
    abstract fun provideAfterRegisterViewModel(afterRegisterViewModel: AfterRegisterViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(PlayerViewModel::class)
    abstract fun providePlayerViewModel(playerViewModel: PlayerViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(PodcastViewModel::class)
    abstract fun providePodcastViewModel(podcastViewModel: PodcastViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(PlayerQueueViewModel::class)
    abstract fun providePlayerQueueViewModel(playerQueueViewModel: PlayerQueueViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(AccountDetailViewModel::class)
    abstract fun provideAccountDetailViewModel(accountDetailViewModel: AccountDetailViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(RecentlyPlayedViewModel::class)
    abstract fun provideRecentlyPlayedViewModel(recentlyPlayedViewModel: RecentlyPlayedViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(FavoritesViewModel::class)
    abstract fun provideFavoritesViewModel(favoritesViewModel: FavoritesViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(PostDetailViewModel::class)
    abstract fun providePostDetailViewModel(postDetailViewModel: PostDetailViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(NewPostViewModel::class)
    abstract fun provideNewPostViewModel(newPostViewModel: NewPostViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(LocaleFeedViewModel::class)
    abstract fun provideLocaleFeedViewModel(localeFeedViewModel: LocaleFeedViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(GlobalFeedViewModel::class)
    abstract fun provideGlobalFeedViewModel(globalFeedViewModel: GlobalFeedViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(FeedSearchViewModel::class)
    abstract fun provideFeedSearchViewModel(feedSearchViewModel: FeedSearchViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(EpisodesViewModel::class)
    abstract fun provideEpisodesViewModel(episodesViewModel: EpisodesViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(PodcastDetailViewModel::class)
    abstract fun providePodcastDetailViewModel(podcastDetailViewModel: PodcastDetailViewModel): ViewModel
}