package com.furkanaskin.app.podpocket.ui.home

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.*
import io.reactivex.Observable

/**
 * Created by Furkan on 16.04.2019
 */

class HomeViewModel(app: Application) : BaseViewModel(app) {

    var bestPodcastsList: List<ChannelsItem?>? = null
    var forceInitBestPodcasts: ObservableField<Boolean> = ObservableField(false)


    init {
        (app as? Podpocket)?.component?.inject(this)
    }

    fun getBestPodcasts(region: String, explicitContent: Int): Observable<BestPodcasts> {
        return baseApi.getBestPodcasts(region, explicitContent)
    }

    fun getPodcastRecommendations(podcastId: String, explicitContent: Int): Observable<PodcastRecommendations> {
        return baseApi.getPodcastRecommendations(podcastId, explicitContent)
    }

    fun getEpisodeRecommendations(podcastId: String, explicitContent: Int): Observable<EpisodeRecommendations> {
        return baseApi.getEpisodeRecommendations(podcastId, explicitContent)
    }

    fun getEpisodes(id: String): Observable<Podcasts> {

        return baseApi.getPodcastById(id)
    }
}