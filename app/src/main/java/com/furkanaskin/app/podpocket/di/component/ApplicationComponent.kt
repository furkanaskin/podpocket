package com.furkanaskin.app.podpocket.di.component

import android.content.Context
import android.content.SharedPreferences
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.di.module.ApplicationModule
import com.furkanaskin.app.podpocket.di.module.DatabaseModule
import com.furkanaskin.app.podpocket.di.module.NetModule
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

    fun inject(dashboardViewModel: DashboardViewModel)

    fun inject(homeViewModel: HomeViewModel)

    fun inject(searchViewModel: SearchViewModel)

    fun inject(feedViewModel: FeedViewModel)

    fun inject(profileViewModel: ProfileViewModel)

    fun inject(settingsViewModel: SettingsViewModel)

    fun inject(afterRegisterViewModel: AfterRegisterViewModel)

    fun inject(playerViewModel: PlayerViewModel)

    fun inject(podcastViewModel: PodcastViewModel)

    fun inject(playerQueueViewModel: PlayerQueueViewModel)

    fun inject(accountDetailViewModel: AccountDetailViewModel)

    fun inject(recentlyPlayedViewModel: RecentlyPlayedViewModel)

    fun inject(favoritesViewModel: FavoritesViewModel)

    fun inject(postDetailViewModel: PostDetailViewModel)

    fun inject(newPostViewModel: NewPostViewModel)

    fun inject(localeFeedViewModel: LocaleFeedViewModel)

    fun inject(globalFeedViewModel: GlobalFeedViewModel)

    fun inject(feedSearchViewModel: FeedSearchViewModel)

    fun inject(episodesViewModel: EpisodesViewModel)

    fun inject(podcastDetailViewModel: PodcastDetailViewModel)
}
