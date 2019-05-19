package com.furkanaskin.app.podpocket.ui.home

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.*
import io.reactivex.Observable
import java.util.*

/**
 * Created by Furkan on 16.04.2019
 */

class HomeViewModel(app: Application) : BaseViewModel(app) {
    var progressBarView: ObservableField<Boolean> = ObservableField(false)

    lateinit var currentLocation: String

    init {
        (app as? Podpocket)?.component?.inject(this)
        getUserLocation()
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

    fun getEpisodeDetails(id: String): Observable<Episode> {

        return baseApi.getEpisodeById(id)
    }

    fun getEpisodes(id: String): Observable<Podcasts> {

        return baseApi.getPodcastById(id)
    }

    private fun getUserLocation() {
        currentLocation = Locale.getDefault().country.toLowerCase()

        if (currentLocation.isNullOrEmpty())
            currentLocation = "us"
    }
}