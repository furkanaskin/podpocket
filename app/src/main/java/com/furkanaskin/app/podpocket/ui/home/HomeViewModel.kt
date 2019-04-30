package com.furkanaskin.app.podpocket.ui.home

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.BestPodcasts
import com.furkanaskin.app.podpocket.service.response.EpisodeRecommendations
import com.furkanaskin.app.podpocket.service.response.PodcastRecommendations
import io.reactivex.Observable

/**
 * Created by Furkan on 16.04.2019
 */

class HomeViewModel(app: Application) : BaseViewModel(app) {
    var progressBarView: ObservableField<Boolean> = ObservableField(false)

    init {
        (app as? Podpocket)?.component?.inject(this)
    }

    fun getBestPodcasts(region: String, explicitContent: Int): Observable<BestPodcasts> {
        return api.getBestPodcasts(region, explicitContent)
    }

    fun getPodcastRecommendations(podcastId: String, explicitContent: Int): Observable<PodcastRecommendations> {
        return api.getPodcastRecommendations(podcastId, explicitContent)
    }

    fun getEpisodeRecommendations(podcastId: String, explicitContent: Int): Observable<EpisodeRecommendations> {
        return api.getEpisodeRecommendations(podcastId, explicitContent)
    }

}