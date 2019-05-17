package com.furkanaskin.app.podpocket.ui.profile.recently_played

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.Episode
import com.furkanaskin.app.podpocket.service.response.Podcasts
import io.reactivex.Observable

/**
 * Created by Furkan on 17.05.2019
 */

class RecentlyPlayedViewModel(app: Application) : BaseViewModel(app) {
    var progressBarView: ObservableField<Boolean> = ObservableField(false)
    var podcastItem: ObservableField<Podcasts> = ObservableField()
    var episodeItem: ObservableField<Episode> = ObservableField()

    init {
        (app as? Podpocket)?.component?.inject(this)
    }

    fun getRecentlyPlayedPodcastDetails(podcastID: String): Observable<Podcasts> {
        return baseApi.getPodcastById(podcastID)
    }

    fun getRecentlyPlayedEpisodeDetails(episodeID: String): Observable<Episode> {
        return baseApi.getEpisodeById(episodeID)
    }
}