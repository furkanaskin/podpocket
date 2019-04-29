package com.furkanaskin.app.podpocket.ui.podcast_episodes

import android.app.Application
import androidx.databinding.ObservableField
import com.furkanaskin.app.podpocket.Podpocket
import com.furkanaskin.app.podpocket.core.BaseViewModel
import com.furkanaskin.app.podpocket.service.response.Episodes
import com.furkanaskin.app.podpocket.service.response.Podcasts
import io.reactivex.Observable

/**
 * Created by Furkan on 29.04.2019
 */

class PodcastEpisodesViewModel(app: Application) : BaseViewModel(app) {
    var progressBarView: ObservableField<Boolean> = ObservableField(false)


    init {
        (app as? Podpocket)?.component?.inject(this)
    }

    fun getEpisodes(id: String): Observable<Podcasts> {

        return api.getPodcastsById(id)
    }


    fun getEpisodeDetails(id: String): Observable<Episodes> {
        return api.getEpisodesById(id)
    }
}