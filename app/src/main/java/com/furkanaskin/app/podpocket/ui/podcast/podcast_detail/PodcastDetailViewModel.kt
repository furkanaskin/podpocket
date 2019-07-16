package com.furkanaskin.app.podpocket.ui.podcast.podcast_detail

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.PodcastRecommendations
import com.furkanaskin.app.podpocket.service.response.Podcasts
import io.reactivex.Observable

/**
 * Created by Furkan on 2019-07-06
 */

class PodcastDetailViewModel(app: Application) : BaseViewModel(app) {

    init {
        (app as? Podpocket)?.component?.inject(this)
    }

    var podcast: ObservableField<Podcasts> = ObservableField()

    fun getPodcastRecommendations(podcastId: String, explicitContent: Int): Observable<PodcastRecommendations> {
        return baseApi.getPodcastRecommendations(podcastId, explicitContent)
    }

}